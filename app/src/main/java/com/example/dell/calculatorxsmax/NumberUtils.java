package com.example.dell.calculatorxsmax;

import java.text.DecimalFormat;

public class NumberUtils {
    public static boolean isIntegerNumber(double x) {
        return x == Math.floor(x) && !Double.isInfinite(x);
    }

    public static String formatNumber(double number) throws NumberFormatException {
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        return decimalFormat.format(number);
    }
}
