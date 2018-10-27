package com.example.dell.calculatorxsmax;

import android.text.TextUtils;

public class Calculation {
    private double mVarTemp;
    private double mVarA;
    private double mResult;
    private EOperator mEOperator;
    private boolean isReadyForCalculation;

    private CalculationCallBack mCalculationCallBack;

    public Calculation() {
        this.mVarA = 0;
        this.mResult = 0;
        this.mEOperator = EOperator.DEFAULT;
    }

    public double getVarA() {
        return mVarA;
    }

    public void setVarA(double varA) {
        mVarA = varA;
    }

    public void setVarA(String varA) {
        try {
            this.mVarA = Double.parseDouble(varA);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public double getResult() {
        return mResult;
    }

    public void setResult(double result) {
        mResult = result;
    }

    public double getVarTemp() {
        return mVarTemp;
    }

    public void setVarTemp(double varTemp) {
        mVarTemp = varTemp;
    }

    public EOperator getEOperator() {
        return mEOperator;
    }

    public void setEOperator(EOperator EOperator) {
        mEOperator = EOperator;
    }

    /* End getter & setter methods */

    public boolean isReadyForCalculation() {
        return isReadyForCalculation;
    }

    public void setReadyForCalculation(boolean readyForCalculation) {
        isReadyForCalculation = readyForCalculation;
    }

    public CalculationCallBack getCalculationCallBack() {
        return mCalculationCallBack;
    }

    public void setCalculationCallBack(CalculationCallBack calculationCallBack) {
        mCalculationCallBack = calculationCallBack;
    }

    public void doCalculation() throws NumberFormatException {
        switch (mEOperator) {
            case PLUS:
                mResult = mVarTemp + mVarA;
                break;
            case MINUS:
                mResult = mVarTemp - mVarA;
                break;
            case MUL:
                mResult = mVarTemp * mVarA;
                break;
            case DIVISION:
                if (mVarA != 0) {
                    mResult = mVarTemp / mVarA;
                } else {
                    mCalculationCallBack.onError("You can't divide by 0");
                }
                break;
            case PERCENTAGE:
                if (mVarA != 0) {
                    mResult = mVarTemp % mVarA;
                } else {
                    mCalculationCallBack.onError("You can't divide by 0");
                }
                break;
            default:
                mVarTemp = mVarA;
                mResult = mVarA;
                return;
        }
        if (mResult != mVarA)
            mCalculationCallBack.showResult(String.valueOf(mResult));
    }

    public void resetAll() {
        mVarA = 0;
        mResult = 0;
        mEOperator = EOperator.DEFAULT;
        mCalculationCallBack.onReset();
    }

    enum EOperator {
        DEFAULT(""),
        PLUS("+"),
        MINUS("-"),
        DIVISION("÷"),
        MUL("×"),
        PERCENTAGE("%");

        String type;

        EOperator(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static EOperator from(String type) {
            for (EOperator eOperator : EOperator.values()) {
                if (!TextUtils.isEmpty(type)) {
                    if (eOperator.type.equals(type)) {
                        return eOperator;
                    }
                }
            }
            return DEFAULT;
        }
    }

}
