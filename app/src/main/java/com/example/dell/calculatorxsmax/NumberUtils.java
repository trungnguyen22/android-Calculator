package com.example.dell.calculatorxsmax;

import java.text.DecimalFormat;

class NumberUtils {
    static boolean isIntegerNumber(double x) {
        return x == Math.floor(x) && !Double.isInfinite(x);
    }

    static String formatNumber(double number) throws NumberFormatException {
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        return decimalFormat.format(number);
    }

    static double parseDoubleNumber(String dNumber) {
        try {
            return Double.parseDouble(dNumber);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
