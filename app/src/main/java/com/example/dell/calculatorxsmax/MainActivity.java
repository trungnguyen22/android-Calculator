package com.example.dell.calculatorxsmax;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Project: Basic Calculator - FUNiX
 * Author: Nguyen Quoc Trung
 * Student ID: FX00077
 * Created: 25/10/2018
 */

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, CalculationCallBack {

    // View members
    TextView mInputTV;
    TextView mResultTV;

    // Data Members
    Calculation mCalculation;

    private int[] mListDigitButtonID = {
            R.id.mDigitOneBtn, R.id.mDigitTwoBtn,
            R.id.mDigitThreeBtn, R.id.mDigitFourBtn,
            R.id.mDigitFiveBtn, R.id.mDigitSixBtn,
            R.id.mDigitSevenBtn, R.id.mDigitEightBtn,
            R.id.mDigitNineBtn, R.id.mDigitZeroBtn
    };

    private int[] mListOperatorButtonID = {
            R.id.mSignToggleBtn, R.id.mDivisionBtn, R.id.mMulBtn,
            R.id.mMinusBtn, R.id.mPlusBtn, R.id.mDotBtn,
            R.id.mPercentageBtn, R.id.mEqualBtn, R.id.mClearBtn
    };


    // Storing user's input
    String first;
    String second;

    // Logical boolean status
    boolean doesFirstHasDot;
    boolean isFirstAddedDot;

    boolean doesSecondHasDot;
    boolean isSecondAddedDot;

    boolean isShownSecond;

    boolean isStartingInsertForSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        bindViews();
        onLandscapeMode();
    }

    private void onLandscapeMode() {
        if (findViewById(R.id.mSqrtBtn) != null) {
            findViewById(R.id.mSqrtBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isStartingInsertForSecond) {
                        onOperatorButtonClicked("");
                        String operator = ((Button) v).getText().toString();
                        mCalculation.setEOperator(Calculation.EOperator.from(operator));
                    } else {
                        mCalculation.setFirst(first);
                        String operator = ((Button) v).getText().toString();
                        mCalculation.setEOperator(Calculation.EOperator.from(operator));
                    }
                    doCalculation();
                    mCalculation.setEOperator(Calculation.EOperator.DEFAULT);
                    displayInput();
                }
            });
        }
    }

    private void initData() {
        first = "";
        second = "";

        doesFirstHasDot = false;
        doesSecondHasDot = false;

        isFirstAddedDot = false;
        isSecondAddedDot = false;

        isShownSecond = false;

        isStartingInsertForSecond = false;

        if (mCalculation == null) {
            mCalculation = new Calculation();
            mCalculation.setCalculationCallBack(this);
        }
    }

    private void bindViews() {
        mInputTV = findViewById(R.id.mInPutTV);
        mResultTV = findViewById(R.id.mResultTV);

        for (int id : mListDigitButtonID) {
            findViewById(id).setOnClickListener(this);
        }

        for (int id : mListOperatorButtonID) {
            findViewById(id).setOnClickListener(this);
        }
    }

    public String getFormatFirst() {
        try {
            double dFirst = Double.parseDouble(first);
            if (doesFirstHasDot && !isFirstAddedDot) {
                isFirstAddedDot = true;
                return NumberUtils.formatNumber(dFirst) + ".";
            }
            if (isFirstAddedDot && NumberUtils.isIntegerNumber(dFirst)) {
                String[] splitFirst = first.split("\\.");
                if (Double.parseDouble(splitFirst[1]) < 1) {
                    return NumberUtils.formatNumber(Double.parseDouble(splitFirst[0])) + "." + splitFirst[1];
                }
            }
            return NumberUtils.formatNumber(dFirst);
        } catch (NumberFormatException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "0";
        }
    }

    public String getFormatSecond() {
        try {
            double dSecond = Double.parseDouble(second);
            if (doesSecondHasDot && !isSecondAddedDot) {
                isSecondAddedDot = true;
                return NumberUtils.formatNumber(dSecond) + ".";
            }
            if (isSecondAddedDot && NumberUtils.isIntegerNumber(dSecond)) {
                String[] splitSecond = second.split("\\.");
                if (Double.parseDouble(splitSecond[1]) < 1) {
                    return NumberUtils.formatNumber(Double.parseDouble(splitSecond[0])) + "." + splitSecond[1];
                }
            }
            return NumberUtils.formatNumber(dSecond);
        } catch (NumberFormatException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "0";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mDigitZeroBtn:
            case R.id.mDigitOneBtn:
            case R.id.mDigitTwoBtn:
            case R.id.mDigitThreeBtn:
            case R.id.mDigitFourBtn:
            case R.id.mDigitFiveBtn:
            case R.id.mDigitSixBtn:
            case R.id.mDigitSevenBtn:
            case R.id.mDigitEightBtn:
            case R.id.mDigitNineBtn:
                String number = (String) findViewById(v.getId()).getTag();
                onDigitNumberButtonClicked(number);
                break;
            case R.id.mPlusBtn:
            case R.id.mMinusBtn:
            case R.id.mDivisionBtn:
            case R.id.mMulBtn:
            case R.id.mPercentageBtn:
                String operator = ((Button) findViewById(v.getId())).getText().toString();
                onOperatorButtonClicked(operator);
                break;
            case R.id.mDotBtn:
                onDotButtonClicked();
                break;
            case R.id.mEqualBtn:
                doCalculation();
                break;
            case R.id.mClearBtn:
                mCalculation.resetAll();
                return;
            case R.id.mSignToggleBtn:
                if (onSignToggleButtonClicked()) return;
                break;
        }
        displayInput();
    }

    private void onDigitNumberButtonClicked(String number) {
        if (!isStartingInsertForSecond) {
            if (first.equals("0") && number.equals("0")) return;
            first = first + number;
        } else {
            if (second.equals("0") && number.equals("0")) return;
            second = second + number;
            isShownSecond = true;
        }
    }

    private void onOperatorButtonClicked(String operator) {
        if (!isStartingInsertForSecond) {
            mCalculation.setFirst(first);
            mCalculation.setEOperator(Calculation.EOperator.from(operator));
        } else {
            doCalculation();
            mCalculation.setEOperator(Calculation.EOperator.from(operator));
        }
        isStartingInsertForSecond = true;
    }

    private void onDotButtonClicked() {
        String dot = ".";
        if (!isStartingInsertForSecond) {
            if (!TextUtils.isEmpty(first)) {
                if (!doesFirstHasDot) {
                    first += dot;
                    doesFirstHasDot = true;
                } else {
                    String[] splitFirst = first.split("\\.");
                    first = "";
                    for (String s : splitFirst) {
                        first += s;
                    }
                    doesFirstHasDot = false;
                    isFirstAddedDot = false;
                }
            }
        } else {
            if (!TextUtils.isEmpty(second)) {
                if (!doesSecondHasDot) {
                    second += dot;
                    doesSecondHasDot = true;
                } else {
                    String[] splitSecond = second.split("\\.");
                    second = "";
                    for (String s : splitSecond) {
                        second += s;
                    }
                    doesSecondHasDot = false;
                    isSecondAddedDot = false;
                }
            }
        }
    }

    private boolean onSignToggleButtonClicked() {
        if (!isStartingInsertForSecond) {
            if (TextUtils.isEmpty(first)) return true;
            double dFirst = NumberUtils.parseDoubleNumber(first);
            first = String.valueOf((dFirst * (-1)));
        } else {
            if (TextUtils.isEmpty(second)) return true;
            double dSecond = NumberUtils.parseDoubleNumber(second);
            second = String.valueOf((dSecond * (-1)));
        }
        return false;
    }

    private void doCalculation() {
        mCalculation.setSecond(second);
        mCalculation.doCalculation();
        doStuffAfterCalculation();
    }

    private void doStuffAfterCalculation() {
        initData();
        first = mCalculation.getFirstAsString();
    }

    public void displayInput() {
        if (isShownSecond) {
            String display = getFormatFirst() + mCalculation.getEOperator().getType() + getFormatSecond();
            mInputTV.setText(display);
        } else {
            String display = getFormatFirst() + mCalculation.getEOperator().getType();
            mInputTV.setText(display);
        }
    }


    @Override
    public void showResult(String result) {
        mResultTV.setText(String.valueOf("Result: " + result));
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(MainActivity.class.getSimpleName(), message);
    }

    @Override
    public void onReset() {
        initData();
        mInputTV.setText("");
        mResultTV.setText("");
    }
}
