package com.example.dell.calculatorxsmax;

public interface CalculationCallBack {

    void showResult(String result);
    void onError(String message);
    void onReset();
}
