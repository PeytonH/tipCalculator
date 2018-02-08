package com.example.skybreaker.tipcalculator_counter_hartzell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    private EditText Amount;
    private EditText numberPeople;
    private EditText customTip;
    private RadioGroup tips;
    private RadioButton tip15;
    private RadioButton tip20;
    private RadioButton tipCustom;
    private Button calculate;
    private Button reset;
    private  TextView totalTip;
    private  TextView totalBill;
    private  TextView totalPerPerson;
    private NumberFormat Format = NumberFormat.getCurrencyInstance();

    private int tipCheck = -1;
    //final static int setNumPeople = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBill = (TextView) findViewById(R.id.toPayText);
        totalTip = (TextView) findViewById(R.id.tipAmountText);
        totalPerPerson = (TextView) findViewById(R.id.perPersonText);
        Amount = (EditText) findViewById(R.id.txtAmount);
        numberPeople = (EditText) findViewById(R.id.txtPeople);
        //numberPeople.setText((Integer.toString(setNumPeople)));
        customTip = (EditText) findViewById(R.id.txtTipOther);
        Amount.requestFocus();
        customTip.setEnabled(false);
        calculate = (Button) findViewById(R.id.calculate);
        calculate.setEnabled(false);
        reset = (Button) findViewById(R.id.resetButton);
        tips = (RadioGroup) findViewById(R.id.RadioGroup);
        tip15 = (RadioButton) findViewById(R.id.radioButton1);
        tip20 = (RadioButton) findViewById(R.id.radioButton2);
        tipCustom = (RadioButton) findViewById(R.id.radioButton3);

        tips.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton1 || i == R.id.radioButton2) {
                    customTip.setEnabled(false);
                    calculate.setEnabled(totalBill.getText().length() > 0 && numberPeople.getText().length() > 0);
                }
                if (i == R.id.radioButton3) {
                    customTip.setEnabled(true);
                    customTip.requestFocus();
                    calculate.setEnabled(totalBill.getText().length() > 0 && numberPeople.getText().length() > 0 && customTip.getText().length() > 0);
                }
                tipCheck = i;

            }
        });

        Amount.setOnKeyListener(newKeyListener);
        numberPeople.setOnKeyListener(newKeyListener);
        customTip.setOnKeyListener(newKeyListener);
        calculate.setOnClickListener(newClickListener);
        reset.setOnClickListener(newClickListener);
    }
        OnKeyListener newKeyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (view.getId())
                {
                    case R.id.txtAmount:
                    case R.id.txtPeople:
                        calculate.setEnabled(Amount.getText().length()>0 && numberPeople.getText().length()>0);
                        break;
                    case R.id.txtTipOther:
                        calculate.setEnabled(totalBill.getText().length()>0 && numberPeople.getText().length() > 0 && customTip.getText().length() >0);
                        break;
                }
                return false;
            }
        };

        private OnClickListener newClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.calculate)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Calculated!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 200);
                    toast.show();
                    calculate();
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Reset!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 200);
                    toast.show();
                    reset();
                }
            }
        };
      private void calculate()
    {
        boolean error = false;
        Double bill = Double.parseDouble(Amount.getText().toString());
        Double numOfPeople = Double.parseDouble(numberPeople.getText().toString());
        Double percentage = null;

        if(bill < 1.00)
        {
            showErrorAlert("Not a valid amount.", Amount.getId());
            error = true;
        }
        if(numOfPeople < 1.00)
        {
            showErrorAlert("Not a valid number of people.", numberPeople.getId());
            error = true;
        }
        if(tipCheck == -1)
        {
            tipCheck = tips.getCheckedRadioButtonId();
        }
        if(tipCheck == R.id.radioButton1)
        {
            percentage = 15.00;
        }
        if(tipCheck == R.id.radioButton2)
        {
            percentage = 20.00;
        }
        if(tipCheck == R.id.radioButton3)
        {
            percentage = Double.parseDouble(tipCustom.getText().toString());
            if(percentage < 1.00)
            {
                showErrorAlert("Not a valid percentage.", tipCustom.getId());
                error = true;
            }
        }
        if(!error)
        {
            Double tip = ((bill * percentage)/100);
            Double newBill = (bill + tip);
            Double soloBill = newBill / numOfPeople;
            //String billString = newBill.toString();
            //String tipString = tip.toString();
            //String soloString = soloBill.toString();

            totalBill.setText(Format.format(newBill));
            totalTip.setText(Format.format(tip));
            totalPerPerson.setText(Format.format(soloBill));
        }
    }
    private void reset()
    {
        totalBill.setText("$0.00");
        totalTip.setText("$0.00");
        totalPerPerson.setText("$0.00");
        Amount.setText("");
        numberPeople.setText("");//Integer.toString(setNumPeople));
        tipCustom.setTag("");
        tips.clearCheck();
        Amount.requestFocus();
    }
    private void showErrorAlert(String errorMessage, final int fieldId) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setNeutralButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                findViewById(fieldId).requestFocus();
                            }
                        }).show();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        totalBill.setText(savedInstanceState.getString("bill"));
        totalTip.setText((savedInstanceState.getString("tipAmountText")));
        totalPerPerson.setText(savedInstanceState.getString("perPersonText"));
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("bill", totalBill.getText().toString());
        outState.putString("tipAmountText", totalTip.getText().toString());
        outState.putString("perPersonText", totalPerPerson.getText().toString());
    }
}

