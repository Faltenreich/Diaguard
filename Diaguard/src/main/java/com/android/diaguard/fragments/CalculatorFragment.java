package com.android.diaguard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.helpers.Validator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 15.11.13.
 */

public class CalculatorFragment extends Fragment {

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    DecimalFormat decimalFormat;

    EditText editTextBloodSugar;
    EditText editTextTargetValue;
    EditText editTextMeal;
    EditText editTextCorrection;
    EditText editTextFactor;

    TextView textViewUnitBloodSugar;
    TextView textViewUnitTargetValue;
    TextView textViewUnitCorrection;
    TextView textViewUnitMeal;

    Spinner spinnerFactors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(getActivity());
        preferenceHelper = new PreferenceHelper(getActivity());
        decimalFormat = Helper.getDecimalFormat();

        getComponents();
        initializeGUI();
    }

    public void getComponents() {
        editTextBloodSugar = (EditText) getView().findViewById(R.id.edittext_bloodsugar);
        editTextTargetValue = (EditText) getView().findViewById(R.id.edittext_target);
        editTextMeal = (EditText) getView().findViewById(R.id.edittext_meal);
        editTextCorrection = (EditText) getView().findViewById(R.id.edittext_correction);
        editTextFactor = (EditText) getView().findViewById(R.id.edittext_factor);
        textViewUnitBloodSugar = (TextView) getView().findViewById(R.id.textview_unit_bloodsugar);
        textViewUnitTargetValue = (TextView) getView().findViewById(R.id.textview_unit_target);
        textViewUnitCorrection = (TextView) getView().findViewById(R.id.textview_unit_correction);
        textViewUnitMeal = (TextView) getView().findViewById(R.id.textview_unit_meal);
        spinnerFactors = (Spinner) getView().findViewById(R.id.spinner_factors);
    }

    public void initializeGUI() {
        String unitAcronym = preferenceHelper.getUnitAcronym(Event.Category.BloodSugar);
        textViewUnitBloodSugar.setText(unitAcronym);
        textViewUnitTargetValue.setText(unitAcronym);
        textViewUnitCorrection.setText(unitAcronym);
        textViewUnitMeal.setText(preferenceHelper.getUnitAcronym(Event.Category.Meal));

        float targetValue = preferenceHelper.getTargetValue();
        editTextTargetValue.setHint(decimalFormat.format(targetValue));

        float correctionValue = preferenceHelper.getCorrectionValue();
        editTextCorrection.setHint(decimalFormat.format(correctionValue));

        spinnerFactors.setSelection(preferenceHelper.getCurrentDaytime().ordinal());
        spinnerFactors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                PreferenceHelper.Daytime daytime = PreferenceHelper.Daytime.values()[position];
                float factor = preferenceHelper.getFactorValue(daytime);
                if(factor == 0)
                    editTextFactor.setHint("");
                else
                    editTextFactor.setHint(decimalFormat.format(factor));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        getView().findViewById(R.id.button_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private boolean validate() {

        boolean isValid = true;
        String bloodSugar = editTextBloodSugar.getText().toString();

        if(!Validator.containsNumber(bloodSugar)) {
            editTextBloodSugar.setError(getString(R.string.validator_value_empty));
            isValid = false;
        }
        else if(!Validator.validateEventValue(getActivity(), Event.Category.BloodSugar,
                preferenceHelper.formatCustomToDefaultUnit(Event.Category.BloodSugar,
                        Float.parseFloat(bloodSugar)))) {
            editTextBloodSugar.setError(getString(R.string.validator_value_unrealistic));
            isValid = false;
        }
        else
            editTextBloodSugar.setError(null);

        if(editTextMeal.length() > 0) {
            if(!Validator.containsNumber(editTextFactor.getText().toString()) &&
                !Validator.containsNumber(editTextFactor.getHint().toString())) {
                editTextFactor.setError(getString(R.string.validator_value_empty));
                isValid = false;
            }
            else
                editTextFactor.setError(null);
        }
        else
            editTextFactor.setError(null);

        return isValid;
    }

    private float calculateBolus(float currentBloodSugar, float targetBloodSugar, float meal, float correction, float factor) {

        float corrector = (currentBloodSugar - targetBloodSugar) / correction;
        float injector = (meal * factor) / 10;

        return corrector + injector;
    }

    private void submit() {

        if(validate()) {

            // Blood Sugar
            final float currentBloodSugar = preferenceHelper.formatCustomToDefaultUnit(Event.Category.BloodSugar, Float.parseFloat(editTextBloodSugar.getText().toString()));

            String targetValueString = editTextTargetValue.getText().toString();
            float targetBloodSugar;
            if(Validator.containsNumber(targetValueString) == false)
                targetBloodSugar = preferenceHelper.getTargetValue();
            else
                targetBloodSugar = Float.parseFloat(targetValueString);
            targetBloodSugar = preferenceHelper.formatCustomToDefaultUnit(Event.Category.BloodSugar, targetBloodSugar);

            float correction;
            if(Validator.containsNumber(editTextCorrection.getText().toString()))
                correction = Float.parseFloat(editTextCorrection.getText().toString());
            else
                correction = Float.parseFloat(editTextCorrection.getHint().toString());

            // Meal
            String mealString = editTextMeal.getText().toString();
            final float meal;
            if(Validator.containsNumber(mealString))
                meal = preferenceHelper.formatCustomToDefaultUnit(Event.Category.Meal, Float.parseFloat(mealString));
            else
                meal = 0;

            float factor = 0;
            if(meal > 0) {
                if(Validator.containsNumber(editTextFactor.getText().toString()))
                    factor = Float.parseFloat(editTextFactor.getText().toString());
                else
                    factor = Float.parseFloat(editTextFactor.getHint().toString());
            }

            showResult(currentBloodSugar, targetBloodSugar, meal, correction, factor);
        }
    }

    private void showResult(float currentBloodSugar, float targetBloodSugar, float meal, float correction, float factor) {

        final float bloodSugar = currentBloodSugar;
        final float mealFinal = meal;
        final float bolus = calculateBolus(currentBloodSugar, targetBloodSugar, meal, correction, factor);

        // Build AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewPopup = inflater.inflate(R.layout.popup_result, null);

        // Show formula for calculating bolus
        TextView textViewFormula = (TextView) viewPopup.findViewById(R.id.textViewFormula);
        textViewFormula.setText("((" + decimalFormat.format(currentBloodSugar) + " mg/dL - " +
                decimalFormat.format(targetBloodSugar) + " mg/dL) / (50 mg/dL * " +
                decimalFormat.format(factor) + ")) + (" + decimalFormat.format(meal/12) +
                " BE * " + decimalFormat.format(factor) + ") =");

        // Handle negative bolus
        TextView textViewInfo = (TextView) viewPopup.findViewById(R.id.textViewInfo);
        if(bolus <= 0) {
            viewPopup.findViewById(R.id.result).setVisibility(View.GONE);
            textViewInfo.setVisibility(View.VISIBLE);
            // TODO: adjust to user settings
            if(bolus < -1)
                textViewInfo.setText(textViewInfo.getText().toString() + " " + getString(R.string.bolus_no2));
        }
        else {
            viewPopup.findViewById(R.id.result).setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.GONE);
        }

        TextView textViewValue = (TextView) viewPopup.findViewById(R.id.textViewResult);
        textViewValue.setText(decimalFormat.format(bolus));

        TextView textViewUnit = (TextView) viewPopup.findViewById(R.id.textViewUnit);
        textViewUnit.setText(preferenceHelper.getUnitAcronym(Event.Category.Bolus));

        final CheckBox checkBoxStoreValues = (CheckBox) viewPopup.findViewById(R.id.checkBoxStoreValues);

        // Custom TitleBar
        View view=inflater.inflate(R.layout.alertdialog_title_bolus, null);
        TextView textViewTitle = (TextView) view.findViewById(R.id.title);
        textViewTitle.setText(getString(R.string.bolus));

        dialogBuilder.setView(viewPopup)
                .setCustomTitle(view)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(checkBoxStoreValues.isChecked())
                            storeValues(bloodSugar, mealFinal, bolus);
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    private void storeValues(float currentBloodSugar, float meal, float bolus) {
        Calendar now = Calendar.getInstance();

        List<Event> events = new ArrayList<Event>();
        Event event = new Event();

        event.setValue(currentBloodSugar);
        event.setDate(now);
        event.setNotes("");
        event.setCategory(Event.Category.BloodSugar);
        events.add(event);

        if(meal > 0) {
            event = new Event();
            event.setValue(meal);
            event.setDate(now);
            event.setNotes("");
            event.setCategory(Event.Category.Meal);
            events.add(event);
        }

        if(bolus > 0) {
            event = new Event();
            event.setValue(bolus);
            event.setDate(now);
            event.setNotes("");
            event.setCategory(Event.Category.Bolus);
            events.add(event);
        }

        dataSource.open();
        dataSource.insertEvents(events);
        dataSource.close();
    }
}