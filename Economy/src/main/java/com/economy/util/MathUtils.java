package com.economy.util;

import com.economy.init.Economy;

public class MathUtils {
    public static float round(float f) {
        return (float) (Math.round(f * Math.pow(10, Economy.getConfig().readInt("decimals"))) / Math.pow(10, Economy.getConfig().readInt("decimals")));
    }

    public static double round(double f) {
        return Math.round(f * Math.pow(10, Economy.getConfig().readInt("decimals"))) / Math.pow(10, Economy.getConfig().readInt("decimals"));
    }

    public static double round(double f, int decimals) {
        return Math.round(f * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }
}
