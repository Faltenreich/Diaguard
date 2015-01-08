package com.faltenreich.diaguard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;

import org.joda.time.DateTime;

/**
 * Created by Filip on 15.11.13.
 */

public class CalculatorActivity extends ActionBarActivity {

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;

    private EditText editTextBloodSugar;
    private EditText editTextTargetValue;
    private EditText editTextMeal;
    private EditText editTextCorrection;
    private EditText editTextFactor;

    private TextView textViewUnitBloodSugar;
    private TextView textViewUnitTargetValue;
    private TextView textViewUnitCorrection;
    private TextView textViewUnitMeal;

    private Spinner spinnerFactors;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        setTitle(getString(R.string.calculator));
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(this);
        preferenceHelper = new PreferenceHelper(this);

        getComponents();
        initializeGUI();
    }

    public void getComponents() {
        editTextBloodSugar = (EditText) findViewById(R.id.edittext_bloodsugar);
        editTextTargetValue = (EditText) findViewById(R.id.edittext_target);
        editTextMeal = (EditText) findViewById(R.id.edittext_meal);
        editTextCorrection = (EditText) findViewById(R.id.edittext_correction);
        editTextFactor = (EditText) findViewById(R.id.edittext_factor);
        textViewUnitBloodSugar = (TextView) findViewById(R.id.textview_unit_bloodsugar);
        textViewUnitTargetValue = (TextView) findViewById(R.id.textview_unit_target);
        textViewUnitCorrection = (TextView) findViewById(R.id.textview_unit_correction);
        textViewUnitMeal = (TextView) findViewById(R.id.textview_unit_meal);
        spinnerFactors = (Spinner) findViewById(R.id.spinner_factors);
    }

    public void initializeGUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.calculator));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(toolbar);
        }

        String unitAcronym = preferenceHelper.getUnitAcronym(Measurement.Category.BloodSugar);
        textViewUnitBloodSugar.setText(unitAcronym);
        textViewUnitTargetValue.setText(unitAcronym);
        textViewUnitCorrection.setText(unitAcronym);
        textViewUnitMeal.setText(preferenceHelper.getUnitAcronym(Measurement.Category.Meal));

        // Target
        float targetValue = preferenceHelper.formatDefaultToCustomUnit(
                Measurement.Category.BloodSugar,
                preferenceHelper.getTargetValue());
        editTextTargetValue.setHint(
                preferenceHelper.getDecimalFormat(Measurement.Category.BloodSugar).format(targetValue));

        // Correction
        float correctionValue = preferenceHelper.formatDefaultToCustomUnit(
                Measurement.Category.BloodSugar,
                preferenceHelper.getCorrectionValue());
                editTextCorrection.setHint(preferenceHelper.
                        getDecimalFormat(Measurement.Category.BloodSugar).format(correctionValue));

        // Factor
        spinnerFactors.setSelection(preferenceHelper.getCurrentDaytime().ordinal());
        spinnerFactors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                PreferenceHelper.Daytime daytime = PreferenceHelper.Daytime.values()[position];
                float factor = preferenceHelper.getFactorValue(daytime);
                if(factor != 0)
                    editTextFactor.setHint(Helper.getDecimalFormat().format(factor));
                else
                    editTextFactor.setHint("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;

        // Blood Sugar
        if(!Validator.validateEditTextEvent(this, editTextBloodSugar, Measurement.Category.BloodSugar))
            isValid = false;

        if(!Validator.validateEditTextEvent(this, editTextTargetValue, Measurement.Category.BloodSugar))
            isValid = false;

        if(!Validator.validateEditTextEvent(this, editTextCorrection, Measurement.Category.BloodSugar))
            isValid = false;

        // Meal
        Editable editableMeal = editTextMeal.getText();
        if(editableMeal == null) {
            throw new Resources.NotFoundException();
        }

        String valueMeal = editableMeal.toString();
        if(valueMeal.length() > 0) {

            if (!Validator.validateEventValue(this, editTextMeal, Measurement.Category.Meal, valueMeal)) {
                isValid = false;
            }

            // Factor
            Editable editableFactor = editTextFactor.getText();
            if(editableFactor == null) {
                throw new Resources.NotFoundException();
            }

            if (!Validator.validateEditTextFactor(this, editTextFactor, false)) {
                isValid = false;
            }
            else {
                editTextFactor.setError(null);
            }
        }

        return isValid;
    }

    private float calculateBolus(float currentBloodSugar, float targetBloodSugar, float meal, float correction, float factor) {
        float corrector = (currentBloodSugar - targetBloodSugar) / correction;
        float injector = (meal * factor) / 10;

        return corrector + injector;
    }

    private void submit() {
        // Blood Sugar
        final float currentBloodSugar =
                preferenceHelper.formatCustomToDefaultUnit(Measurement.Category.BloodSugar,
                        Float.parseFloat(editTextBloodSugar.getText().toString()));

        String targetValueString = editTextTargetValue.getText().toString();
        float targetBloodSugar;
        if(!Validator.containsNumber(targetValueString))
            targetBloodSugar = preferenceHelper.formatDefaultToCustomUnit(
                    Measurement.Category.BloodSugar, preferenceHelper.getTargetValue());
        else
            targetBloodSugar = Float.parseFloat(targetValueString);
        targetBloodSugar = preferenceHelper.formatCustomToDefaultUnit(Measurement.Category.BloodSugar, targetBloodSugar);

        Editable editableText = editTextCorrection.getText();
        CharSequence charSequenceHint = editTextCorrection.getHint();
        float correction;
        if(editableText != null && Validator.containsNumber(editableText.toString())) {
            String correctionText = editableText.toString();
            correction = Float.parseFloat(correctionText);
        }
        else if(charSequenceHint != null && Validator.containsNumber(charSequenceHint.toString())) {
            String correctionHint = charSequenceHint.toString();
            correction = Float.parseFloat(correctionHint);
        }
        else
            return;
        correction = preferenceHelper.formatCustomToDefaultUnit(Measurement.Category.BloodSugar, correction);

        // Meal
        String mealString = editTextMeal.getText().toString();
        final float meal;
        if(Validator.containsNumber(mealString))
            meal = preferenceHelper.formatCustomToDefaultUnit(Measurement.Category.Meal, Float.parseFloat(mealString));
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

    private void showResult(float currentBloodSugar, float targetBloodSugar, float meal, float correction, float factor) {

        final float bloodSugar = currentBloodSugar;
        final float mealFinal = meal;
        final float bolus = calculateBolus(currentBloodSugar, targetBloodSugar, meal, correction, factor);

        // Build AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View viewPopup = inflater.inflate(R.layout.popup_result, null);

        // Handle negative bolus
        TextView textViewInfo = (TextView) viewPopup.findViewById(R.id.textViewInfo);
        if(bolus <= 0) {
            viewPopup.findViewById(R.id.result).setVisibility(View.GONE);
            textViewInfo.setVisibility(View.VISIBLE);
            // TODO: adjust to user settings
            if(bolus < -1)
                textViewInfo.setText(textViewInfo.getText().toString() + " " +
                        getString(R.string.bolus_no2));
        }
        else {
            viewPopup.findViewById(R.id.result).setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.GONE);
        }

        TextView textViewValue = (TextView) viewPopup.findViewById(R.id.textViewResult);
        textViewValue.setText(Helper.getDecimalFormat().format(bolus));

        TextView textViewUnit = (TextView) viewPopup.findViewById(R.id.textViewUnit);
        textViewUnit.setText(preferenceHelper.getUnitAcronym(Measurement.Category.Bolus));

        final CheckBox checkBoxStoreValues = (CheckBox) viewPopup.findViewById(R.id.checkBoxStoreValues);

        // Custom TitleBar
        View view = inflater.inflate(R.layout.alertdialog_title_bolus, null);
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
                        finish();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    private void storeValues(float currentBloodSugar, float meal, float bolus) {
        dataSource.open();

        Entry entry = new Entry();
        entry.setDate(new DateTime());
        long entryId = dataSource.insert(entry);

        Measurement measurement = new Measurement();
        measurement.setValue(currentBloodSugar);
        measurement.setCategory(Measurement.Category.BloodSugar);
        measurement.setEntryId(entryId);
        dataSource.insert(measurement);

        if(meal > 0) {
            measurement = new Measurement();
            measurement.setValue(meal);
            measurement.setCategory(Measurement.Category.Meal);
            measurement.setEntryId(entryId);
            dataSource.insert(measurement);
        }

        if(bolus > 0) {
            measurement = new Measurement();
            measurement.setValue(bolus);
            measurement.setCategory(Measurement.Category.Bolus);
            measurement.setEntryId(entryId);
            dataSource.insert(measurement);
        }

        dataSource.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                if(validate())
                    submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}