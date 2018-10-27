package com.example.dell.calculatorxsmax;

public class NumberUtils {
    public static boolean isIntegerNumber(double x) {
        return x == Math.floor(x) && !Double.isInfinite(x);
    }
}
