package com.example.dell.calculatorxsmax;

import android.text.TextUtils;

public class Calculation {
    private String mVarA;
    private String mVarB;
    private String mResult;

    private EOperator mEOperator;

    private boolean isReadyForCalculation;

    public String getVarA() {
        return mVarA;
    }

    public void setVarA(String varA) {
        mVarA = varA;
    }

    public String getVarB() {
        return mVarB;
    }

    public void setVarB(String varB) {
        mVarB = varB;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public EOperator getEOperator() {
        return mEOperator;
    }

    public void setEOperator(EOperator EOperator) {
        mEOperator = EOperator;
    }

    /* End getter & setter methods */

    public boolean isVarAEmpty() {
        return TextUtils.isEmpty(mVarA);
    }

    public boolean isReadyForCalculation() {
        return isReadyForCalculation;
    }

    public void setReadyForCalculation(boolean readyForCalculation) {
        isReadyForCalculation = readyForCalculation;
    }

    public void doCalculation() throws NumberFormatException {
        if (isReadyForCalculation) {
            double result = 0;
            switch (mEOperator) {
                case PLUS:
                    result = Double.parseDouble(mVarA) + Double.parseDouble(mVarB);
                    break;
                case MINUS:
                    result = Double.parseDouble(mVarA) - Double.parseDouble(mVarB);
                    break;
                case MUL:
                    result = Double.parseDouble(mVarA) * Double.parseDouble(mVarB);
                    break;
                case DIVISION:
                    result = Double.parseDouble(mVarA) / Double.parseDouble(mVarB);
                    break;
            }
            mResult = String.valueOf(result);
        }
    }

    enum EOperator {
        PLUS("+"),
        MINUS("-"),
        DIVISION("÷"),
        MUL("×");

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
            return null;
        }
    }

}
