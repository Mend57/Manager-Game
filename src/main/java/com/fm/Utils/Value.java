package com.fm.Utils;

public class Value {

    public static int normalize(int number, int limit){
        double normalized = (number * 20.0) / limit;
        if(normalized > limit) return limit;
        if (normalized <= 1) return 1;
        return (int)Math.round(normalized);
    }

    public static double normalize(double number, double limit){
        double normalized = (number * 20.0) / limit;
        if(normalized > limit) return limit;
        if (normalized <= 1.0) return 1.0;
        return normalized;
    }

}
