package com.example.dell.calculatorxsmax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

    // Logical member
    String mOperator;
    String mVarA;
    String mStorePreviousNumber;
    String mSign;

    // Two variable for showing input from user to screen
    String displayCalculation;
    String storeFirstExpression;

    private int[] mListDigitButtonID = {
            R.id.mDigitOneBtn, R.id.mDigitTwoBtn,
            R.id.mDigitThreeBtn, R.id.mDigitFourBtn,
            R.id.mDigitFiveBtn, R.id.mDigitSixBtn,
            R.id.mDigitSevenBtn, R.id.mDigitEightBtn,
            R.id.mDigitNineBtn, R.id.mDigitZeroBtn
    };

    private int[] mListOperatorButtonID = {
            R.id.mPositiveNegativeToggleBtn, R.id.mDivisionBtn, R.id.mMulBtn,
            R.id.mMinusBtn, R.id.mPlusBtn, R.id.mCommaBtn,
            R.id.mPercentageBtn, R.id.mEqualBtn, R.id.mClearBtn
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        bindViews();
    }

    private void initData() {
        mSign = "+";
        mOperator = "";
        mVarA = "";
        displayCalculation = "";
        storeFirstExpression = "";

        // Object calculator is for saving result and doing calculation
        mCalculation = new Calculation();
        mCalculation.setCalculationCallBack(this);
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
                // Get the number as a string from the button by its tag
                String number = (String) findViewById(v.getId()).getTag();
                onDigitNumberButtonClicked(number);
                break;
            case R.id.mPlusBtn:
            case R.id.mMinusBtn:
            case R.id.mDivisionBtn:
            case R.id.mMulBtn:
            case R.id.mPercentageBtn:
                // Get the operator as a string from the button by its text
                String newOperator = ((Button) findViewById(v.getId())).getText().toString();
                if (!mOperator.equals(newOperator)) {
                    mOperator = newOperator;
                    onOperatorClicked();
                }
                break;
            case R.id.mCommaBtn:
                onCommaButtonClicked();
                break;
            case R.id.mEqualBtn:
                onEqualClicked();
                break;
            case R.id.mClearBtn:
                onClearClicked();
                break;
            case R.id.mPositiveNegativeToggleBtn:
                onToggleSignNumber();
                break;
        }
    }

    /**
     * Handle click event when user click on digit number button.
     *
     * @param number - is the number of the digit number button clicked on.
     */
    private void onDigitNumberButtonClicked(String number) throws NumberFormatException {
        if (!TextUtils.isEmpty(mVarA)) {
            if (Double.parseDouble(mVarA) == 0 && Double.parseDouble(number) == 0) {
                return;
            }
        }
        mVarA += number;
        displayCalculation += number;
        mOperator = "";
        doCalculation();
        displayInput();
    }

    /**
     * Handle click event when user click on operator button
     */
    private void onOperatorClicked() {
        mCalculation.setEOperator(Calculation.EOperator.from(mOperator));
        mCalculation.setVarTemp(mCalculation.getResult());

        mVarA = "";

        double result = mCalculation.getResult();
        displayCalculation = NumberUtils.formatNumber(result) + mOperator;
        storeFirstExpression = displayCalculation;
        displayInput();
    }

    /**
     * Handle when user click on +/- button to toggle the sign of a number
     * In case of positive or negative sign
     */
    private void onToggleSignNumber() {
        if (!TextUtils.isEmpty(mVarA)) {
            try {
                double varA = Double.parseDouble(mVarA);
                if (varA > 0) {
                    mVarA = "-" + mVarA;
                } else {
                    varA = varA * (-1);
                    if (NumberUtils.isIntegerNumber(varA))
                        mVarA = String.valueOf((int) varA);
                    else
                        mVarA = String.valueOf(varA);
                }
                doCalculation();
                // update new expression
                displayCalculation = storeFirstExpression + NumberUtils.formatNumber(Double.parseDouble(mVarA));
                displayInput();
            } catch (NumberFormatException e) {
                onError(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle when user click on comma button
     */
    private void onCommaButtonClicked() {
        if (!TextUtils.isEmpty(mVarA)) {
            mVarA += ".";
            displayCalculation += ".";
            displayInput();
        }
    }

    private void onClearClicked() {
        mCalculation.resetAll();
    }

    private void onEqualClicked() {
        mVarA = NumberUtils.formatNumber(mCalculation.getResult());

        // When we press equal button, that means we show the result of the last expression
        // And also reset some variable as a new one
        displayCalculation = mVarA;
        storeFirstExpression = "";
        mCalculation.setEOperator(Calculation.EOperator.DEFAULT);

        mInputTV.setText(mVarA);
    }

    /* End of onClick methods */

    /**
     * This method is for showing user's input or expression on the screen by setting text for the TextView
     */
    private void displayInput() {
        mInputTV.setText(displayCalculation);
    }

    /**
     * This method is for doing calculation
     */
    private void doCalculation() {
        mCalculation.setVarA(mVarA);
        mCalculation.doCalculation();
    }


    /* All methods below are callbacks from Calculation to MainActivity */

    /**
     * When calculation is finished and ready to show on the screen. Calculation class will do the callback out there
     */
    @Override
    public void showResult(String result) {
        mResultTV.setText("Result: " + result);
    }

    /**
     * Whenever calculation is not valid. Calculation class will callback there
     */
    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * When user click on AC (All clear), so Calculation instance will clear all its data, and callback there
     */
    @Override
    public void onReset() {
        storeFirstExpression = "";
        mOperator = "";
        displayCalculation = "";
        mVarA = "";
        mStorePreviousNumber = "";
        mInputTV.setText("");
        mResultTV.setText("");
    }
}
