package com.example.dell.calculatorxsmax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mInputTV;
    TextView mResultTV;

    Calculation mCalculation;

    String displayCalculation;

    Expression mExpression;

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
            R.id.mEqualBtn, R.id.mClearBtn
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        bindViews();
    }

    private void initData() {
        displayCalculation = "";
        mCalculation = new Calculation();
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
            case R.id.mCommaBtn:
                String operator = (String) findViewById(v.getId()).getTag();
                onOperatorClicked(operator);
                break;
            case R.id.mEqualBtn:
                onEqualClicked();
                break;
            case R.id.mClearBtn:
                onClearClicked();
                break;
        }
    }

    private void onDigitNumberButtonClicked(String number) {

    }

    private void onOperatorClicked(String operator) {

    }

    private void onClearClicked() {

    }

    private void onEqualClicked() {

    }

    private void displayInput() {
        mInputTV.setText(displayCalculation);
    }

    private void displayResult() {
        mExpression = new ExpressionBuilder(displayCalculation).build();
        mResultTV.setText(String.valueOf(mExpression.evaluate()));
    }

}
