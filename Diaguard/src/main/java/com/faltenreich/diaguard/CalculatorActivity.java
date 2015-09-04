package com.faltenreich.diaguard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
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

import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;

import org.joda.time.DateTime;

import butterknife.Bind;

/**
 * Created by Filip on 15.11.13.
 */

public class CalculatorActivity extends BaseActivity {

    @Bind(R.id.edittext_bloodsugar)
    protected EditText editTextBloodSugar;

    @Bind(R.id.edittext_target)
    protected EditText editTextTargetValue;

    @Bind(R.id.edittext_meal)
    protected EditText editTextMeal;

    @Bind(R.id.edittext_correction)
    protected EditText editTextCorrection;

    @Bind(R.id.edittext_factor)
    protected EditText editTextFactor;

    @Bind(R.id.textview_unit_bloodsugar)
    protected TextView textViewUnitBloodSugar;

    @Bind(R.id.textview_unit_target)
    protected TextView textViewUnitTargetValue;

    @Bind(R.id.textview_unit_correction)
    protected TextView textViewUnitCorrection;

    @Bind(R.id.textview_unit_meal)
    protected TextView textViewUnitMeal;

    @Bind(R.id.spinner_factors)
    protected Spinner spinnerFactors;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_calculator;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {
        String unitAcronym = PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BloodSugar);
        textViewUnitBloodSugar.setText(unitAcronym);
        textViewUnitTargetValue.setText(unitAcronym);
        textViewUnitCorrection.setText(unitAcronym);
        textViewUnitMeal.setText(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.Meal));

        // Target
        float targetValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BloodSugar,
                PreferenceHelper.getInstance().getTargetValue());
        editTextTargetValue.setHint(
                PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(targetValue));

        // Correction
        float correctionValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BloodSugar,
                PreferenceHelper.getInstance().getCorrectionValue());
                editTextCorrection.setHint(PreferenceHelper.getInstance().
                        getDecimalFormat(Measurement.Category.BloodSugar).format(correctionValue));

        // Factor
        spinnerFactors.setSelection(PreferenceHelper.getInstance().getCurrentDaytime().ordinal());
        spinnerFactors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                PreferenceHelper.Daytime daytime = PreferenceHelper.Daytime.values()[position];
                float factor = PreferenceHelper.getInstance().getFactorValue(daytime);
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
                PreferenceHelper.getInstance().formatCustomToDefaultUnit(Measurement.Category.BloodSugar,
                        Float.parseFloat(editTextBloodSugar.getText().toString()));

        String targetValueString = editTextTargetValue.getText().toString();
        float targetBloodSugar;
        if(!Validator.containsNumber(targetValueString))
            targetBloodSugar = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                    Measurement.Category.BloodSugar, PreferenceHelper.getInstance().getTargetValue());
        else
            targetBloodSugar = Float.parseFloat(targetValueString);
        targetBloodSugar = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Measurement.Category.BloodSugar, targetBloodSugar);

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
        correction = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Measurement.Category.BloodSugar, correction);

        // Meal
        String mealString = editTextMeal.getText().toString();
        final float meal;
        if(Validator.containsNumber(mealString))
            meal = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Measurement.Category.Meal, Float.parseFloat(mealString));
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

        // Handle negative insulin
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
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.Insulin));

        final CheckBox checkBoxStoreValues = (CheckBox) viewPopup.findViewById(R.id.checkBoxStoreValues);

        // Custom TitleBar
        View view = inflater.inflate(R.layout.alertdialog_title_bolus, null);
        TextView textViewTitle = (TextView) view.findViewById(R.id.title);
        textViewTitle.setText(getString(R.string.insulin));

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

        Entry entry = new Entry();
        entry.setDate(new DateTime());
        //TODO
        /*
        long entryId = dataSource.insert(entry);

        Measurement measurement = new Measurement();
        measurement.setValue(currentBloodSugar);
        measurement.setCategory(Measurement.Category.BloodSugar);
        measurement.setEntry(entryId);
        dataSource.insert(measurement);

        if(meal > 0) {
            measurement = new Measurement();
            measurement.setValue(meal);
            measurement.setCategory(Measurement.Category.Meal);
            measurement.setEntry(entryId);
            dataSource.insert(measurement);
        }

        if(bolus > 0) {
            measurement = new Measurement();
            measurement.setValue(bolus);
            measurement.setCategory(Measurement.Category.Insulin);
            measurement.setEntry(entryId);
            dataSource.insert(measurement);
        }
        */
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