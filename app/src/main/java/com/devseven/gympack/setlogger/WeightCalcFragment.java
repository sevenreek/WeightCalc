package com.devseven.gympack.setlogger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Dickbutt on 05.06.2017.
 */

public class WeightCalcFragment extends Fragment {
    public static final String ARG_CURRENT_WEIGHT = "CURRENT_WEIGHT";
    public static final String WEIGHTCALC_TAG = "WEIGHTCALC_TAG444";
    public static final String LINEAR_POSITION = "LINEAR_POSITION";
    public interface IWeightCalculator
    {
        double getWeight();
        void setWeight(double weight);

    }
    IWeightCalculator myInterfaceActivity;
    Activity myActivity;
    double currentWeight;
    boolean imperialUnits;
    double increment;
    double barWeight;
    int position;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.weigh_editor_inset,container,false);
        if (savedInstanceState != null) {
            increment = savedInstanceState.getDouble("INCREMENT_VALUE", 2.5);
            imperialUnits = savedInstanceState.getBoolean("USE_IMPERIAL", false);
            currentWeight = savedInstanceState.getDouble(ARG_CURRENT_WEIGHT, 40);// 40 is for testing only
            barWeight = savedInstanceState.getDouble("BAR_WEIGHT", 20);
            position = savedInstanceState.getInt(LINEAR_POSITION);
        }
        else if (getArguments()!=null)
        {
            Bundle args = getArguments();
            increment = args.getDouble("INCREMENT_VALUE", 2.5);
            imperialUnits = args.getBoolean("USE_IMPERIAL", false);
            currentWeight = args.getDouble(ARG_CURRENT_WEIGHT, 40); // 40 is for testing only
            barWeight = args.getDouble("BAR_WEIGHT", 20);
            position = args.getInt(LINEAR_POSITION);
        }
        else
        {
            increment = 2.5;
            imperialUnits = false;
            currentWeight = 0;
            barWeight = 20;
        }
        if(getActivity() instanceof IWeightCalculator)
        {
            myInterfaceActivity = (IWeightCalculator)getActivity();
        }
        else
        {
            Log.e("FATAL ERROR!",getActivity().toString()+" must implement IWeightCalculator interface!");
        }
        field1 = (TextView) view.findViewById(R.id.weightedit_text1);
        field1.setHorizontallyScrolling(true);
        field2 = (TextView) view.findViewById(R.id.weightedit_text2);
        field3 = (TextView) view.findViewById(R.id.weightedit_text3);
        ImageView button_less = (ImageView)view.findViewById(R.id.weightedit_less);
        button_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeight-=increment;
                if(currentWeight<0)
                    currentWeight=0;
                myInterfaceActivity.setWeight(currentWeight);
                UpdateWeights();
            }
        });

        ImageView button_more = (ImageView)view.findViewById(R.id.weightedit_more);
        button_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeight+=increment;
                myInterfaceActivity.setWeight(currentWeight);
                UpdateWeights();
            }
        });
        button_less.setClickable(true);
        button_more.setClickable(true);
        return view;
    }
    TextView field1;
    TextView field2;
    TextView field3;
    public WeightCalcFragment()
    {}
    DecimalFormat df;



    @Override
    public void onStart()
    {
        df = new DecimalFormat();
        df.setDecimalSeparatorAlwaysShown(false);
        super.onStart();
        myActivity = getActivity();

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

        // IT IS VITAL THAT THE ARRAY IS SORTED BEFORE UPDATE WEIGHTS.
        // IT MAKES MORE SENSE TO SORT IT WHEN ADDING NEW WEIGHTS NOT EVERY SINGLE TIME
        UpdateWeights();
    }
    @Override
    public  void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putDouble(ARG_CURRENT_WEIGHT, currentWeight);
        super.onSaveInstanceState(savedInstanceState);
    }
    String fieldText_1;
    String fieldText_2;
    String fieldText_3;

    public void UpdateWeights() {

        int counter = 1;
        fieldText_1 = new String();
        fieldText_2 = new String();
        fieldText_3 = new String();
        fieldText_1+=barWeight+"kg\u00a0bar\n";
        if (Weight.Weights != null) {
            // If the weight is less than the bar(20kg) then there is no reason to iterate.
            //if(currentWeight<barWeight)
            //   return;
            double remainingWeight = (currentWeight - barWeight) / 2.0;

            for (int i = 0; i < Weight.Weights.size(); i++) {
                int platesAmount = 0;
                Weight currentPlate = Weight.Weights.get(i);
                if (currentPlate.getPlateWeight() > remainingWeight)
                    currentPlate.setTimesUsed(0);
                else {
                    platesAmount = (int) (remainingWeight / currentPlate.getPlateWeight());
                    if (platesAmount > (currentPlate.getPlateAmount()/2)) // if there is not enough plates of this type
                        platesAmount = currentPlate.getPlateAmount()/2; // set it to max. available
                    currentPlate.setTimesUsed(platesAmount);
                    remainingWeight -= platesAmount * currentPlate.getPlateWeight();

                }
                if (platesAmount > 0) {
                    counter++;
                    if(counter>8)
                    {
                        fieldText_3+=platesAmount+"x"+df.format(currentPlate.getPlateWeight())+"kg\n";
                    }
                    else if(counter>4)
                    {
                        fieldText_2+=platesAmount+"x"+df.format(currentPlate.getPlateWeight())+"kg\n";
                    }
                    else
                    {
                        fieldText_1+=platesAmount+"x"+df.format(currentPlate.getPlateWeight())+"kg\n";
                    }
                }


            }

            if (remainingWeight > 0) {
                if(counter>7) {
                    fieldText_3 += ("+" + df.format(remainingWeight) + "kg");
                }
                else if(counter>3) {
                    fieldText_2 += ("+" + df.format(remainingWeight) + "kg");
                }
                else {
                    fieldText_1 += ("+" + df.format(remainingWeight) + "kg");
                }
            }
            else if (remainingWeight < 0) {
                if(counter>7) {
                    fieldText_3 += ("-" + df.format(remainingWeight) + "kg");
                }
                else if(counter>3) {
                    fieldText_2 += ("-" + df.format(remainingWeight) + "kg");
                }
                else {
                    fieldText_1 += ("-" + df.format(remainingWeight) + "kg");
                }
            }
            if(counter>=8&&remainingWeight!=0)
                field3.setVisibility(View.VISIBLE);
            else
                field3.setVisibility(View.GONE);

            field1.setText(fieldText_1);
            field2.setText(fieldText_2);
            field3.setText(fieldText_3);
        }
    }


}
