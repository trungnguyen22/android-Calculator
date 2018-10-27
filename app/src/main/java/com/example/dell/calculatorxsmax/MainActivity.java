package com.example.dell.calculatorxsmax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    String storeFirstString;

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
                String number = (String) findViewById(v.getId()).getTag();
                onDigitNumberButtonClicked(number);
                break;
            case R.id.mPlusBtn:
            case R.id.mMinusBtn:
            case R.id.mDivisionBtn:
            case R.id.mMulBtn:
            case R.id.mPercentageBtn:
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

    private void onOperatorClicked() {
        mCalculation.setEOperator(Calculation.EOperator.from(mOperator));
        mCalculation.setVarTemp(mCalculation.getResult());

        mVarA = "";

        double result = mCalculation.getResult();
        if (NumberUtils.isIntegerNumber(result)) {
            // cast to integer type
            displayCalculation = (int) result + mOperator;
        } else {
            displayCalculation = result + mOperator;
        }
        // Store the first expression, for later use.
        storeFirstString = displayCalculation;
        displayInput();
    }

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
                displayCalculation = storeFirstString + mVarA;
                displayInput();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

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

        if (NumberUtils.isIntegerNumber(mCalculation.getResult())) {
            mVarA = String.valueOf((int) mCalculation.getResult());
        } else {
            mVarA = String.valueOf(mCalculation.getResult());
        }
        displayCalculation = mVarA;
        storeFirstString = "";
        mInputTV.setText(mVarA);
        mCalculation.setEOperator(Calculation.EOperator.DEFAULT);
    }

    /* End of onClick methods */

    private void displayInput() {
        mInputTV.setText(displayCalculation);
    }

    private void doCalculation() {
        mCalculation.setVarA(mVarA);
        mCalculation.doCalculation();
    }

    @Override
    public void showResult(String result) {
        mResultTV.setText("Result: " + result);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReset() {
        storeFirstString = "";
        mOperator = "";
        displayCalculation = "";
        mVarA = "";
        mStorePreviousNumber = "";
        mInputTV.setText("");
        mResultTV.setText("");
    }
}
