package com.example.marcus.lab1;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.view.View;
import android.text.InputType;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the EditText field and the binary RadioButton
        EditText editText = (EditText) findViewById(R.id.entry);
        RadioButton binary = (RadioButton) findViewById(R.id.binary);

        //set the keyboard to number input
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        //check the binary radio button on app start up
        binary.setChecked(true);
    }

    public void clearNumber(View view) {
        //get the required fields for clearing
        EditText editText = (EditText) findViewById(R.id.entry);
        TextView binary = (TextView) findViewById(R.id.binRes);
        TextView octal = (TextView) findViewById(R.id.octRes);
        TextView decimal = (TextView) findViewById(R.id.decRes);
        TextView hex = (TextView) findViewById(R.id.hexRes);

        //clearing the EditText was required
        editText.setText("");

        //cleared all the conversion text for cleanliness
        binary.setText("0");
        octal.setText("0");
        decimal.setText("0");
        hex.setText("0");
    }

    public void onRadioButtonClicked(View view) {

        //make sure the button is checked
        boolean checked = ((RadioButton) view).isChecked();

        //get the EditText field
        EditText editText = (EditText) findViewById(R.id.entry);

        /*
         * get the current RadioButton id and assign either a number keyboard or a text keyboard
         * based the allowed input for the selected base.
         */
        switch (view.getId()) {
            case R.id.binary:
                if(checked) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                break;
            case R.id.octal:
                if(checked) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                break;
            case R.id.decimal:
                if(checked) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                break;
            case R.id.hex:
                if(checked) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
        }
    }

    public void convertValues(View view) {

        //get the required fields for converting
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioButtons);
        TextView binary = (TextView) findViewById(R.id.binRes);
        TextView octal = (TextView) findViewById(R.id.octRes);
        TextView decimal = (TextView) findViewById(R.id.decRes);
        TextView hex = (TextView) findViewById(R.id.hexRes);
        EditText editText = (EditText) findViewById(R.id.entry);

        //get the string of text from the EditText field
        String text = editText.getText().toString();


        /*
         * checks to see if the keyboard is open or not
         * and if it is, the keyboard is closed.
         */
        View checkView = this.getCurrentFocus();
        if (checkView != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(checkView.getWindowToken(), 0);
        }

        //create the SnackBar message for catching the error of parseInt
        Snackbar snackbar;

        /*
         * based upon the selected RadioButton do the necessary conversion from base
         * and then set the TextViews of the corresponding converted bases,
         * if an error is caught then display the SnackBar message and N/A the conversions
         *
         * String.format(Locale, ...) is used here instead of Integer.toString(...) to silence
         * warnings about different languages.
         */
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.binary:
                try {
                    int number = Integer.parseInt(text, 2);
                    binary.setText(Integer.toBinaryString(number));
                    octal.setText(Integer.toOctalString(number));
                    decimal.setText(String.format(Locale.getDefault(), "%d", number));
                    hex.setText(Integer.toHexString(number));
                } catch(Exception e) {
                    binary.setText("N/A");
                    octal.setText("N/A");
                    decimal.setText("N/A");
                    hex.setText("N/A");
                    snackbar = Snackbar.make(view, "Input value (" + text + ") is not valid for base 2", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
            case R.id.octal:
                try {
                    int number = Integer.parseInt(text, 8);
                    binary.setText(Integer.toBinaryString(number));
                    octal.setText(Integer.toOctalString(number));
                    decimal.setText(String.format(Locale.getDefault(), "%d", number));
                    hex.setText(Integer.toHexString(number));
                } catch(Exception e) {
                    binary.setText("N/A");
                    octal.setText("N/A");
                    decimal.setText("N/A");
                    hex.setText("N/A");
                    snackbar = Snackbar.make(view, "Input value (" + text + ") is not valid for base 8", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
            case R.id.decimal:
                try {
                    int number = Integer.parseInt(text, 10);
                    binary.setText(Integer.toBinaryString(number));
                    octal.setText(Integer.toOctalString(number));
                    decimal.setText(String.format(Locale.getDefault(), "%d", number));
                    hex.setText(Integer.toHexString(number));
                } catch(Exception e) {
                    binary.setText("N/A");
                    octal.setText("N/A");
                    decimal.setText("N/A");
                    hex.setText("N/A");
                    snackbar = Snackbar.make(view, "Input value (" + text + ") is not valid for base 10", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
            case R.id.hex:
                try {
                    int number = Integer.parseInt(text, 16);
                    binary.setText(Integer.toBinaryString(number));
                    octal.setText(Integer.toOctalString(number));
                    decimal.setText(String.format(Locale.getDefault(), "%d", number));
                    hex.setText(Integer.toHexString(number));
                } catch(Exception e) {
                    binary.setText("N/A");
                    octal.setText("N/A");
                    decimal.setText("N/A");
                    hex.setText("N/A");
                    snackbar = Snackbar.make(view, "Input value (" + text + ") is not valid for base 16", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
        }
    }
}
