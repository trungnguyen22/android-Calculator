package com.example.dell.calculatorxsmax;

import android.text.TextUtils;

class Calculation {
    private double mFirst;
    private double mSecond;
    private double mResult;
    private EOperator mEOperator;

    private CalculationCallBack mCalculationCallBack;

    Calculation() {
        this.mFirst = 0;
        this.mSecond = 0;
        this.mResult = 0;
        this.mEOperator = EOperator.DEFAULT;
    }

    String getFirstAsString() {
        if (NumberUtils.isIntegerNumber(mFirst)) {
            return String.valueOf((int) mFirst);
        } else {
            return String.valueOf(mFirst);
        }
    }

    void setFirst(String first) {
        try {
            this.mFirst = Double.parseDouble(first);
        } catch (NumberFormatException e) {
            mCalculationCallBack.onError(e.getMessage());
            e.printStackTrace();
        }
    }

    void setSecond(String second) {
        try {
            this.mSecond = Double.parseDouble(second);
        } catch (NumberFormatException e) {
            mCalculationCallBack.onError(e.getMessage());
            e.printStackTrace();
        }
    }

    EOperator getEOperator() {
        return mEOperator;
    }

    void setEOperator(EOperator EOperator) {
        mEOperator = EOperator;
    }

    void setCalculationCallBack(CalculationCallBack calculationCallBack) {
        mCalculationCallBack = calculationCallBack;
    }

    void doCalculation() throws NumberFormatException {
        switch (mEOperator) {
            case PLUS:
                mResult = mFirst + mSecond;
                break;
            case MINUS:
                mResult = mFirst - mSecond;
                break;
            case MUL:
                mResult = mFirst * mSecond;
                break;
            case DIVISION:
                if (mSecond != 0) {
                    mResult = mFirst / mSecond;
                } else {
                    mCalculationCallBack.onError("You can't divide by 0");
                }
                break;
            case PERCENTAGE:
                if (mSecond != 0) {
                    mResult = mFirst % mSecond;
                } else {
                    mCalculationCallBack.onError("You can't divide by 0");
                }
                break;
            case SQRT:
                if (mFirst != -1) {
                    mResult = Math.sqrt(mFirst);
                } else {
                    mCalculationCallBack.onError("You can't not √(-1)");
                }
        }
        doStuffAfterCalculation();
    }

    private void doStuffAfterCalculation() {
        mFirst = mResult;
        mResult = 0;
        mSecond = 0;
        mEOperator = EOperator.DEFAULT;
        mCalculationCallBack.showResult(NumberUtils.formatNumber(mFirst));
    }

    void resetAll() {
        mFirst = 0;
        mSecond = 0;
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
        PERCENTAGE("%"),
        SQRT("√");

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
