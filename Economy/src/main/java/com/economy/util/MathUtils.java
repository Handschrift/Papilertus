package com.economy.util;

public class MathUtils {
    public static float round(float f) {
        return (float) (Math.round(f * Math.pow(10, 2)) / Math.pow(10, 2));
    }

    public static double round(double f) {
        return Math.round(f * Math.pow(10, 2)) / Math.pow(10, 2);
    }
}
