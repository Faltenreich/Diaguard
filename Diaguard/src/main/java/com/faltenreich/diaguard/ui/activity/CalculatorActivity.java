package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.Validator;
import com.faltenreich.diaguard.util.event.Events;
import com.faltenreich.diaguard.util.event.data.EntryAddedEvent;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Filip on 15.11.13.
 */

public class CalculatorActivity extends BaseActivity {

    @BindView(R.id.edittext_bloodsugar) EditText editTextBloodSugar;
    @BindView(R.id.edittext_target) EditText editTextTargetValue;
    @BindView(R.id.edittext_meal) EditText editTextMeal;
    @BindView(R.id.edittext_correction) EditText editTextCorrection;
    @BindView(R.id.edittext_factor) EditText editTextFactor;
    @BindView(R.id.textview_unit_bloodsugar) TextView textViewUnitBloodSugar;
    @BindView(R.id.textview_unit_target) TextView textViewUnitTargetValue;
    @BindView(R.id.textview_unit_correction) TextView textViewUnitCorrection;
    @BindView(R.id.textview_unit_meal) TextView textViewUnitMeal;
    @BindView(R.id.spinner_factors) Spinner spinnerFactors;

    public CalculatorActivity() {
        super(R.layout.activity_calculator);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                calculate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initialize() {
        String unitAcronym = PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR);
        textViewUnitBloodSugar.setText(unitAcronym);
        textViewUnitTargetValue.setText(unitAcronym);
        textViewUnitCorrection.setText(unitAcronym);
        textViewUnitMeal.setText(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.MEAL));

        // Target
        float targetValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getTargetValue());
        editTextTargetValue.setHint(Helper.parseFloat(targetValue));

        // Correction
        float correctionValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getCorrectionValue());
        editTextCorrection.setHint(Helper.parseFloat(correctionValue));

        // Factor
        spinnerFactors.setSelection(PreferenceHelper.getInstance().getCurrentDaytime().ordinal());
        spinnerFactors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                PreferenceHelper.Daytime daytime = PreferenceHelper.Daytime.values()[position];
                float factor = PreferenceHelper.getInstance().getFactorValue(daytime);
                if (factor != 0)
                    editTextFactor.setHint(Helper.parseFloat(factor));
                else
                    editTextFactor.setHint("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private boolean inputIsValid() {
        boolean isValid = true;

        // Blood Sugar
        if (!Validator.validateEditTextEvent(this, editTextBloodSugar, Measurement.Category.BLOODSUGAR)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(this, editTextTargetValue, Measurement.Category.BLOODSUGAR)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(this, editTextCorrection, Measurement.Category.BLOODSUGAR)) {
            isValid = false;
        }

        // Meal
        String meal = editTextMeal.getText().toString();
        if (meal.length() > 0) {
            if (!Validator.validateEventValue(this, editTextMeal, Measurement.Category.MEAL, meal)) {
                isValid = false;
            }
            // Factor
            if (!Validator.validateEditTextFactor(this, editTextFactor, false)) {
                isValid = false;
            } else {
                editTextFactor.setError(null);
            }
        }
        return isValid;
    }

    private void calculate() {
        if (inputIsValid()) {
            float currentBloodSugar =
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            Measurement.Category.BLOODSUGAR,
                            Float.parseFloat(editTextBloodSugar.getText().toString()));
            float targetBloodSugar =
                    Validator.containsNumber(editTextTargetValue.getText().toString()) ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    Measurement.Category.BLOODSUGAR,
                                    Float.parseFloat(editTextTargetValue.getText().toString())) :
                            PreferenceHelper.getInstance().getTargetValue();
            float correction =
                    Validator.containsNumber(editTextCorrection.getText().toString()) ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    Measurement.Category.BLOODSUGAR,
                                    Float.parseFloat(editTextCorrection.getText().toString())) :
                            PreferenceHelper.getInstance().getCorrectionValue();
            float meal =
                    Validator.containsNumber(editTextMeal.getText().toString()) ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    Measurement.Category.MEAL,
                                    Float.parseFloat(editTextMeal.getText().toString())) :
                            0;
            float factor = 0;
            if (meal > 0) {
                if (Validator.containsNumber(editTextFactor.getText().toString())) {
                    factor = Float.parseFloat(editTextFactor.getText().toString());
                } else if (Validator.containsNumber(editTextFactor.getHint().toString())) {
                    factor = Float.parseFloat(editTextFactor.getHint().toString());
                }
            }

            float insulinBolus = (meal * factor) / 10;
            float insulinCorrection = (currentBloodSugar - targetBloodSugar) / correction;

            StringBuilder builderFormula = new StringBuilder();
            StringBuilder builderFormulaContent = new StringBuilder();

            if (insulinBolus > 0) {
                String carbohydrateAcronym = getResources().getStringArray(R.array.meal_units_acronyms)[1];
                builderFormula.append(String.format("%s * %s",
                        carbohydrateAcronym,
                        getString(R.string.factor)));
                builderFormulaContent.append(String.format("%s %s * %s",
                        Helper.parseFloat(meal / 10),
                        carbohydrateAcronym,
                        factor));
            }

            if (builderFormulaContent.length() > 0) {
                builderFormula.append(" + ");
                builderFormulaContent.append(" + ");
            }
            builderFormula.append(String.format("(%s - %s) / %s",
                    getString(R.string.bloodsugar),
                    getString(R.string.pref_therapy_targets_target),
                    getString(R.string.correction_value)));
            String bloodSugarUnit = PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR);
            builderFormulaContent.append(String.format("(%s %s - %s %s) / %s %s",
                    Helper.parseFloat(currentBloodSugar), bloodSugarUnit,
                    Helper.parseFloat(targetBloodSugar), bloodSugarUnit,
                    Helper.parseFloat(correction), bloodSugarUnit));

            builderFormula.append(" =");
            builderFormulaContent.append(" =");

            showResult(builderFormula.toString(), builderFormulaContent.toString(),
                    currentBloodSugar, meal, insulinBolus, insulinCorrection);
        }
    }

    // Values are normalized
    private void showResult(String formula, String formulaContent, final float bloodSugar, final float meal, final float bolus, final float correction) {

        float insulin = bolus + correction;

        // Build AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View viewPopup = inflater.inflate(R.layout.dialog_calculator_result, null);

        TextView textViewFormula = (TextView) viewPopup.findViewById(R.id.dialog_calculator_result_formula);
        textViewFormula.setText(formula);

        TextView textViewFormulaContent = (TextView) viewPopup.findViewById(R.id.dialog_calculator_result_formula_content);
        textViewFormulaContent.setText(formulaContent);

        // Handle negative insulin
        TextView textViewInfo = (TextView) viewPopup.findViewById(R.id.textViewInfo);
        if (insulin <= 0) {
            // Advice skipping bolus
            viewPopup.findViewById(R.id.result).setVisibility(View.GONE);
            textViewInfo.setVisibility(View.VISIBLE);
            if (insulin < -1) {
                // Advice consuming carbohydrates
                textViewInfo.setText(String.format("%s %s", textViewInfo.getText().toString(), getString(R.string.bolus_no2)));
            }
        } else {
            viewPopup.findViewById(R.id.result).setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.GONE);
        }

        TextView textViewValue = (TextView) viewPopup.findViewById(R.id.textViewResult);
        textViewValue.setText(Helper.parseFloat(insulin));

        TextView textViewUnit = (TextView) viewPopup.findViewById(R.id.textViewUnit);
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.INSULIN));

        dialogBuilder.setView(viewPopup)
                .setTitle(R.string.bolus)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.store_values, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        storeValues(bloodSugar, meal, bolus, correction);
                        finish();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    private void storeValues(float mgDl, float carbohydrates, float bolus, float correction) {
        DateTime now = DateTime.now();
        Entry entry = new Entry();
        entry.setDate(now);
        EntryDao.getInstance().createOrUpdate(entry);

        BloodSugar bloodSugar = new BloodSugar();
        bloodSugar.setMgDl(mgDl);
        bloodSugar.setEntry(entry);
        MeasurementDao.getInstance(BloodSugar.class).createOrUpdate(bloodSugar);

        if (carbohydrates > 0) {
            Meal meal = new Meal();
            meal.setCarbohydrates(carbohydrates);
            meal.setEntry(entry);
            MeasurementDao.getInstance(Meal.class).createOrUpdate(meal);
        }

        if (bolus > 0 || correction > 0) {
            Insulin insulin = new Insulin();
            insulin.setBolus(bolus);
            insulin.setCorrection(correction);
            insulin.setEntry(entry);
            MeasurementDao.getInstance(Insulin.class).createOrUpdate(insulin);
        }

        Events.post(new EntryAddedEvent(entry));
    }
}