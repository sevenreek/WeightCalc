package com.devseven.gympack.setlogger;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.view.ViewGroup.LayoutParams;


public class WeightCalculator extends AppCompatActivity {

    // TODO UPDATE WEIGHTCALCULATOR ACTIVITY WITH WEIGHCALCULATORFRAGMENT METHODS AND BEHAVIOUR
    double currentWeight;
    boolean imperialUnits;
    double increment;
    double barWeight;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if Bundle exists grab currentWeight and increment from it.
        if (savedInstanceState != null) {
            increment = savedInstanceState.getDouble("INCREMENT_VALUE", 2.5);
            imperialUnits = savedInstanceState.getBoolean("USE_IMPERIAL", false);
            currentWeight = savedInstanceState.getDouble("CURRENT_WEIGHT", 0);
            barWeight = savedInstanceState.getDouble("BAR_WEIGHT", 20);
        }
        else
        {
            increment = 2.5;
            imperialUnits = false;
            currentWeight = 0;
            barWeight = 20;
        }
        // DO NOT PUT findViewById ABOVE THIS POINT. THE APP WILL CRASH
        setContentView(R.layout.activity_weight_calculator);
        linearLayout = (LinearLayout) findViewById(R.id.TextHolder);
        // Find views and assign
        final EditText weightAmountEditText = (EditText) findViewById(R.id.weightText);
        Button increase = (Button) findViewById(R.id.increaseButton);
        Button decrease = (Button) findViewById(R.id.decreaseButton);
        increase.setText("+" + Double.toString(increment));
        decrease.setText("-" + Double.toString(increment));

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeight += increment;
                weightAmountEditText.setText(Double.toString(currentWeight));
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeight -= increment;
                weightAmountEditText.setText(Double.toString(currentWeight));

            }
        });

        weightAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            // Add everything that has to be done when the amount changes here
            @Override
            public void afterTextChanged(Editable s) {
                if(!weightAmountEditText.getText().toString().isEmpty()){
                    currentWeight = Double.parseDouble(weightAmountEditText.getText().toString());
                    UpdateWeights();
                }
                else
                    currentWeight = 0;


            }
        });

       // WEIGHTPLATE TEST MODE!!!!!!!!!
        if(Weight.Weights==null) {
            new Weight(25, 2);
            new Weight(20, 2);
            new Weight(15, 2);
            new Weight(10, 2);
            new Weight(5, 2);
            new Weight(2.5, 2);
            new Weight(1.25, 2);
        }

    }
    // This updates the needed weights, TextViews etc.

    void UpdateWeights()
    {
        linearLayout.removeAllViews();
        TextView bar = new TextView(this);
        bar.setId(0);
        bar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        bar.setText(barWeight+"kg bar");
        linearLayout.addView(bar);
        Log.d("D","UpdateWeights called. CurrentWeight is: "+currentWeight);
        if(Weight.Weights!=null) {
            // If the weight is less than the bar(20kg) then there is no reason to iterate.
            //if(currentWeight<barWeight)
            //   return;
            double remainingWeight = (currentWeight-barWeight)/2.0;

            for (int i = 0; i < Weight.Weights.size(); i++) {
                int platesAmount = 0;
                Weight currentPlate = Weight.Weights.get(i);
                if(currentPlate.getPlateWeight()>remainingWeight)
                    currentPlate.setTimesUsed(0);
                else {
                    platesAmount = (int) (remainingWeight / currentPlate.getPlateWeight());
                    if(platesAmount>currentPlate.getPlateAmount())
                        platesAmount = currentPlate.getPlateAmount();
                    currentPlate.setTimesUsed(platesAmount);
                    remainingWeight -= platesAmount * currentPlate.getPlateWeight();

                }
                if(platesAmount>0)
                {
                    TextView weightValue = new TextView(this);
                    weightValue.setId(40+i);
                    weightValue.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                    weightValue.setText(platesAmount+" x "+currentPlate.getPlateWeight()+"kg plate");
                    linearLayout.addView(weightValue);
                }


            }
            Log.d("","Weight remaining: "+remainingWeight);
            if(remainingWeight>0)
            {

                TextView tv = new TextView(this);
                tv.setId(0);
                tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                tv.setText("This setup is LIGHTER than the goal weight by "+ remainingWeight+"kgs on each side.");
                tv.setTextColor(Color.rgb(255,0,0));
                linearLayout.addView(tv);
            }

            else if (remainingWeight<0)
            {
                TextView tv = new TextView(this);
                tv.setId(0);
                tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                tv.setText("This setup is HEAVIER than the goal weight by "+ remainingWeight*2+".");
                tv.setTextColor(Color.rgb(0,255,0));
                linearLayout.addView(tv);
            }
       }



    }

}
