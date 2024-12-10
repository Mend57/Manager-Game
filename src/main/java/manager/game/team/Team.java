package manager.game.team;

import lombok.Getter;
import manager.game.player.Goalkeeper;
import manager.game.player.Outfield;
import manager.game.player.Player;
import manager.game.player.Position;
import lombok.AccessLevel;
import lombok.Setter;
import manager.game.utils.Value;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class Team {
    private final int id;
    private final String name;
    private List<Player> players;
    @Setter(AccessLevel.NONE)
    private Player[] mainPlayers = new Player[11], reservePlayers = new Player[7];

    private Formation formation;
    @Setter(AccessLevel.NONE)
    private Map<Formation, Double> formationFamiliarity = new HashMap<>();

    @Setter(AccessLevel.NONE)
    private double salaryBudget, transactionBudget;
    private double salaryCost;

    @Setter(AccessLevel.NONE)
    private int goals = 0, goalsAgainst = 0, goalsBalance = 0, wins = 0, losses = 0, draws = 0, points = 0;
    private int division;

    public Team(int id, String name, List<Player> players, double salaryBudget, double transactionBudget, int division, Formation initialFormation) {
        this.id = id;
        this.name = name;
        this.players = players;
        this.salaryBudget = salaryBudget;
        setSalaryCost();
        this.transactionBudget = transactionBudget;
        this.division = division;
        this.formation = initialFormation;
        autoMainSquad(formation);
        for (Formation formation : Formation.values()) {
            formationFamiliarity.put(formation, 0.0);
        }
    }

    public List<Goalkeeper> getGoalkeepers() {
        List<Goalkeeper> goalkeepers = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Goalkeeper && !player.isInjury()) {
                goalkeepers.add((Goalkeeper) player);
            }
        }
        return goalkeepers;
    }
    public List<Outfield> getOutfielders() {
        List<Outfield> outfielders = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Outfield && !player.isInjury()) {
                outfielders.add((Outfield) player);
            }
        }
        return outfielders;
    }
    public List<Outfield> getPlayersInPosition(Position position) {
        List<Outfield> playersInPosition = new ArrayList<>();
        for (Outfield player : getOutfielders()) {
            if (player.getPosition().equals(position) && !player.isInjury()) {
                playersInPosition.add(player);
            }
        }
        return playersInPosition;
    }

    public void setPoints() {
        points = wins * 3 + draws;
    }
    public void setGoalsBalance() {
        this.goalsBalance = goals - goalsAgainst;
    }
    public void setSalaryCost() {
        for (Player player : players) {
            salaryCost += player.getSalary();
        }
    }
    public void addGoals(int goals){
        this.goals += goals;
    }
    public void addGoalsAgainst(int goalsAgainst){
        this.goalsAgainst += goalsAgainst;
    }
    public void addWins(int wins){
        this.wins += wins;
    }
    public void addLosses(int losses){
        this.losses += losses;
    }
    public void addDraws(int draws){
        this.draws += draws;
    }
    public void addTransactionBudget(double value){
        this.transactionBudget += value;
    }
    public void removeTransactionBudget(double value){
        this.transactionBudget -= value;
    }
    public void addSalaryBudget(double value){
        this.salaryBudget += value;
    }
    public void removeSalaryBudget(double value){
        this.salaryBudget -= value;
    }
    public void addFormationFamiliarity(Formation formation, double value) {
        Double currentFamiliarity = formationFamiliarity.get(formation);
        formationFamiliarity.put(formation, currentFamiliarity + value);
    }



    public void autoMainSquad(Formation currentFormation) {
        final int expectedDefensorsNumber = currentFormation.getFormation().get("DEFENSE") , expectedMidfieldersNumber = currentFormation.getFormation().get("MIDFIELD"),
                expectedAttackersNumber = currentFormation.getFormation().get("ATTACK");

        Player[] mainDefensePlayers = new Player[expectedDefensorsNumber], mainMidfieldPlayers = new Player[expectedMidfieldersNumber], mainAttackPlayers = new Player[expectedAttackersNumber];

        Map<Player, Double> outfieldersCompetenceMap = new HashMap<>(), defensorsCompetenceMap = new HashMap<>(), attackersCompetenceMap = new HashMap<>(),
                            midfieldersCompetenceMap = new HashMap<>(), goalkeepersCompetenceMap = new HashMap<>();

        for (Player player : getGoalkeepers()) goalkeepersCompetenceMap.put(player, player.competence());
        for (Player player : getOutfielders()) outfieldersCompetenceMap.put(player, player.competence());
        for (Player player : getPlayersInPosition(Position.DEFENSE)) defensorsCompetenceMap.put(player, player.competence());
        for (Player player : getPlayersInPosition(Position.MIDFIELD)) midfieldersCompetenceMap.put(player, player.competence());
        for (Player player : getPlayersInPosition(Position.ATTACK)) attackersCompetenceMap.put(player, player.competence());

        //Sort by competence
        Map<Player, Double> goalkeepersSorted = sortPlayersByCompetence(goalkeepersCompetenceMap), outfieldersSorted = sortPlayersByCompetence(outfieldersCompetenceMap),
                            defensorsSorted = sortPlayersByCompetence(defensorsCompetenceMap), midfieldersSorted = sortPlayersByCompetence(midfieldersCompetenceMap),
                            attackersSorted = sortPlayersByCompetence(attackersCompetenceMap);

        // Select main goalkeeper for mainPlayers and reservePlayers
        boolean hasGoalKeeper = false;
        for (Player player : goalkeepersSorted.keySet()) {
            if (!hasGoalKeeper) {
                mainPlayers[0] = player;
                goalkeepersSorted.remove(player);
                hasGoalKeeper = true;
            } else {
                reservePlayers[0] = player;
                goalkeepersSorted.remove(player);
                break;
            }
        }

        selectMainPlayersByPosition(defensorsSorted, outfieldersSorted, expectedDefensorsNumber, mainDefensePlayers);
        selectMainPlayersByPosition(midfieldersSorted, outfieldersSorted, expectedMidfieldersNumber, mainMidfieldPlayers);
        selectMainPlayersByPosition(attackersSorted, outfieldersSorted, expectedAttackersNumber, mainAttackPlayers);

        concatenateMainPlayers(mainDefensePlayers, mainMidfieldPlayers, mainAttackPlayers);
        fillRemainingSlots(defensorsSorted, midfieldersSorted, attackersSorted, outfieldersSorted, expectedDefensorsNumber);
        selectReservePlayers(goalkeepersSorted, outfieldersSorted, defensorsSorted, midfieldersSorted, attackersSorted);
    }

    private void fillRemainingSlots(Map<Player, Double> defensorsSorted, Map<Player, Double> midfieldersSorted, Map<Player, Double> attackersSorted,
                                    Map<Player, Double> outfieldersSorted, int expectedDefensorsNumber) {

        for (int i = 1; i < mainPlayers.length; i++) {
            if (!outfieldersSorted.isEmpty()) {
                if (mainPlayers[i] == null) {
                    Outfield playerMidfield = (Outfield) midfieldersSorted.keySet().iterator().next();
                    Outfield player = (Outfield) outfieldersSorted.keySet().iterator().next();
                    if (midfieldersSorted.isEmpty()) {
                        mainPlayers[i] = player;
                        outfieldersSorted.remove(player);
                        if (i > expectedDefensorsNumber) player.setCurrentPosition(Position.ATTACK);
                        else player.setCurrentPosition(Position.DEFENSE);

                        if (player.getPosition() == Position.DEFENSE) defensorsSorted.remove(player);
                        else attackersSorted.remove(player);

                    } else {
                        mainPlayers[i] = playerMidfield;
                        outfieldersSorted.remove(playerMidfield);
                        midfieldersSorted.remove(playerMidfield);
                        playerMidfield.setCurrentPosition(Position.MIDFIELD);
                    }
                }
            } else break;
        }
    }


    private void selectMainPlayersByPosition(Map<Player, Double> sortedMap, Map<Player, Double> outfieldersSorted, int expectedNumberOfPlayers, Player[] mainPlayersInPosition) {

        int index = 0;
        for (Player outfielder : sortedMap.keySet()) {
            Outfield player = (Outfield) outfielder;
            if (index < expectedNumberOfPlayers) {
                mainPlayersInPosition[index++] = player;
                sortedMap.remove(player);
                outfieldersSorted.remove(player);
                player.setCurrentPosition(player.getPosition());
            } else break;
        }
    }

    private void selectReservePlayers(Map<Player, Double> goalkeepersSorted, Map<Player, Double> outfieldersSorted, Map<Player, Double> defensorsSorted,
                                      Map<Player, Double> midfieldersSorted, Map<Player, Double> attackersSorted) {

        for (int i = 0; i < reservePlayers.length; i++ ) {
            if (!goalkeepersSorted.isEmpty() || !outfieldersSorted.isEmpty()) {
                if(reservePlayers[i] == null) {
                    Goalkeeper goalkeeper = (Goalkeeper) goalkeepersSorted.keySet().iterator().next();
                    Outfield player = (Outfield) outfieldersSorted.keySet().iterator().next();
                    if (!outfieldersSorted.isEmpty()) {
                        reservePlayers[i] = player;
                        outfieldersSorted.remove(player);
                        switch (player.getPosition()){
                            case DEFENSE:
                                defensorsSorted.remove(player);
                                break;
                            case MIDFIELD:
                                midfieldersSorted.remove(player);
                                break;
                            case ATTACK:
                                attackersSorted.remove(player);
                                break;
                        }

                    }
                    else{
                        reservePlayers[i] = goalkeeper;
                        goalkeepersSorted.remove(goalkeeper);
                    }
                }
            } else break;
        }
    }

    private Map<Player, Double> sortPlayersByCompetence(Map<Player, Double> competenceMap){
        return competenceMap.entrySet().stream()
                .sorted(Map.Entry.<Player, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private void concatenateMainPlayers(Player[] defensePlayers, Player[] midfieldPlayers, Player[] attackPlayers) {
        for (int i = 1; i < mainPlayers.length; i++){
            if(i <= defensePlayers.length){
                mainPlayers[i] = defensePlayers[i - 1];
            }
            else if(i <= defensePlayers.length + midfieldPlayers.length){
                mainPlayers[i] = midfieldPlayers[i - 1 - defensePlayers.length];
            }
            else if(i <= defensePlayers.length + midfieldPlayers.length + attackPlayers.length){
                mainPlayers[i] = attackPlayers[i - 1 - defensePlayers.length - midfieldPlayers.length];

            }
        }
    }

    public double teamCompetence() {
        double mainCompetence = 0;
        double reserveCompetence = 0;
        int numOfMainPlayers = mainPlayers.length;
        int numOfReservePlayers = reservePlayers.length;
            for (Player player : mainPlayers) {
                if (player instanceof Outfield) mainCompetence += player.inGameCompetence();
                else mainCompetence += player.inGameCompetence() / 2;
            }
            for (Player player : reservePlayers) {
                if (player instanceof Outfield) mainCompetence += player.inGameCompetence();
                else mainCompetence += player.inGameCompetence() / 2;
            }
        return Value.normalize(mainCompetence + reserveCompetence / 2,
                        (numOfReservePlayers * Value.getMINIMUM_ATTRIBUTES()) / 4.0 + (numOfMainPlayers-1) * Value.getMINIMUM_ATTRIBUTES() + Value.getMINIMUM_ATTRIBUTES() / 2.0,
                        (numOfReservePlayers * Value.getATTRIBUTES_THRESHOLD()) / 2.0 + (numOfMainPlayers-1) * Value.getATTRIBUTES_THRESHOLD()) + Value.getATTRIBUTES_THRESHOLD() / 2.0;
    }
}
