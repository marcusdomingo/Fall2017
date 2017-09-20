package com.example.white.unitconvert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private int position = 0;   // will use this to keep track of which conversion
    // is chosen in the spinner

    // These are the multiplication factors used in the converter.  Note the two
    // placeholders for the temp conversions - these are special cases
    private double[] multipliers = {
            0.0015625,		// Acres to square miles
            101325.0,		// Atmospheres to Pascals
            100000.0,		// Bars to Pascals
            0,				// placeholder for celsius to farhenheit
            0,				// placeholder for farhenheit to celsius
            0.00001,		// Dynes to Newtons
            0.3048,			// feet/sec to meters/second
            0.0284130625,	// ounces (UK) to Liters
            0.0295735295625, // ounces (US) to Liters
            746.0,			// Horsepower (electric) to Watts
            735.499,		// Horsepower (metric) to Watts
            1/1016.0469088,	// Kilograms to Tons (UK)
            1/907.18474,	// Kilograms to Tons (US)
            1/0.0284130625,	// liters to fluid ounces (UK)
            1/0.0295735295625, // liters to fluid ounces	(US)
            331.5,			// Mach Number to Meters/second
            1/0.3048, 		// Meters/second to Feet/second
            1/331.5,			// Meters/second to Mach number
            0.833,			// Miles/Gallon (UK) to Miles/Gallon (US)
            1/0.833,		// Miles/Gallon (US) to Miles/Gallon (UK)
            100000.0,		// Newtons to Dynes
            1/101325.0,		// Pascals to Atmospheres
            0.00001,		// Pascals to Bars
            640.0,			// Square miles to acres
            1016.0469088,	// Tons (UK) to Kilograms
            907.18474,		// Tons (US) to Kilograms
            1/746.0,		// Watts to Horsepower (electric)
            1/735.499		// Watts to Horsepower (metric)

    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this is where the user inputs the number to be converted
        final EditText etUnits = (EditText) findViewById(R.id.units);

        // The spinner - create the spinner and use the conversions array to
        // populate the choices
        final Spinner spnConversions = (Spinner)findViewById(R.id.conversions);
        ArrayAdapter<CharSequence> aa;
        aa = ArrayAdapter.createFromResource(this,
                R.array.conversions,
                android.R.layout.simple_spinner_item);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnConversions.setAdapter(aa);

        AdapterView.OnItemSelectedListener oisl;
        oisl = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {  MainActivity.this.position = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("nothing");
            }
        };
        spnConversions.setOnItemSelectedListener(oisl);

        // We have three buttons: clear, convert and close

        // The clear button  - starts out disabled.  When enabled, clicking the button
        // sets the editable field to empty.
        final Button btnClear = (Button) findViewById(R.id.clear);
        AdapterView.OnClickListener ocl;
        ocl = new AdapterView.OnClickListener() {
            public void onClick(View v) {
                etUnits.setText("");
            }
        };
        btnClear.setOnClickListener(ocl);
        btnClear.setEnabled(false);

        // The convert button - initially disabled.  Once enabled, it does the
        // conversion specified by position - notice that the temperature conversions
        // position 3 & 4, are handled separately.  Puts the answer in the editable field
        final Button btnConvert = (Button) findViewById(R.id.convert);
        ocl = new AdapterView.OnClickListener() {
            public void onClick(View v) {
                String text = etUnits.getText().toString();
                double input = Double.parseDouble(text);
                double result = 0;
                if (position == 3)
                    result = input*9.0/5.0 + 32; // Celsius to Fahrenheit
                else if (position == 4)
                    result = (input - 32)* 5.0/ 9.0; // Fahrenheit to Celsius
                else
                    result = input * multipliers[position];
                etUnits.setText(""+result);
            }
        };
        btnConvert.setOnClickListener(ocl);
        btnConvert.setEnabled(false);

        // The close button - always enabled.  Terminates the application by calling finish()
        Button btnClose = (Button) findViewById(R.id.close);
        ocl = new AdapterView.OnClickListener() {
            public void onClick(View v) { finish(); }
        };
        btnClose.setOnClickListener(ocl);

        // Toggles the button enabling - by knowing when there is information to convert
        TextWatcher tw;
        tw = new TextWatcher(){
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (etUnits.getText().length() == 0) {
                    btnClear.setEnabled(false);
                    btnConvert.setEnabled(false);
                } else {
                    btnClear.setEnabled(true);
                    btnConvert.setEnabled(true);
                }

            }
        };
        etUnits.addTextChangedListener(tw);
    }

}