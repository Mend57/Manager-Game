package manager.game.utils;

public class MyArray {

    public static <T> T[] concatenateArrays(T[] array1, T[] array2) {
        T[] concatenatedArray = (T[]) new Object[array1.length + array2.length];
        for (int i = 0; i < array1.length; i++){
            concatenatedArray[i] = array1[i];
        }
        for (int i = 0; i < array2.length; i++){
            concatenatedArray[array1.length + i] = array2[i];
        }
        return concatenatedArray;
    }


}
