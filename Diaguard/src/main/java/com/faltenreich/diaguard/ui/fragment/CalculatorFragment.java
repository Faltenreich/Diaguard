package com.faltenreich.diaguard.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.ui.view.FoodInputView;
import com.faltenreich.diaguard.ui.view.StickyHintInput;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.Validator;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 10.09.2016.
 */
public class CalculatorFragment extends BaseFragment {

    @BindView(R.id.calculator_bloodsugar) StickyHintInput bloodSugarInput;
    @BindView(R.id.calculator_target) StickyHintInput targetInput;
    @BindView(R.id.calculator_correction) StickyHintInput correctionInput;
    @BindView(R.id.calculator_food_list_view)
    FoodInputView foodInputView;
    @BindView(R.id.calculator_factor) StickyHintInput factorInput;

    public CalculatorFragment() {
        super(R.layout.fragment_calculator, R.string.calculator);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.form, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    private void initialize() {
        updateTargetValue();
        updateCorrectionValue();
        updateFactor();
    }

    private void updateTargetValue() {
        float targetValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getTargetValue());
        targetInput.setText(Helper.parseFloat(targetValue));
    }

    private void updateCorrectionValue() {
        float correctionValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getCorrectionValue());
        correctionInput.setText(Helper.parseFloat(correctionValue));
    }

    private void updateFactor() {
        int hourOfDay = DateTime.now().getHourOfDay();
        float factor = PreferenceHelper.getInstance().getFactorForHour(hourOfDay);
        factorInput.setText(factor >= 0 ? Helper.parseFloat(factor) : null);
    }

    private boolean inputIsValid() {
        boolean isValid = true;

        // Blood Sugar
        if (!Validator.validateEditTextEvent(getContext(), bloodSugarInput.getEditText(), Measurement.Category.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(getContext(), targetInput.getEditText(), Measurement.Category.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(getContext(), correctionInput.getEditText(), Measurement.Category.BLOODSUGAR, false)) {
            isValid = false;
        }

        // Meal
        if (foodInputView.getTotalCarbohydrates() > 0) {
            // Factor
            if (!Validator.validateEditTextFactor(getContext(), factorInput.getEditText(), false)) {
                isValid = false;
            } else {
                factorInput.setError(null);
            }
        }

        return isValid;
    }

    private void calculate() {
        if (inputIsValid()) {
            float currentBloodSugar =
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            Measurement.Category.BLOODSUGAR,
                            NumberUtils.parseNumber(bloodSugarInput.getText().toString()));
            float targetBloodSugar =
                    Validator.containsNumber(targetInput.getText().toString()) ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    Measurement.Category.BLOODSUGAR,
                                    NumberUtils.parseNumber(targetInput.getText().toString())) :
                            PreferenceHelper.getInstance().getTargetValue();
            float correction =
                    Validator.containsNumber(correctionInput.getText().toString()) ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    Measurement.Category.BLOODSUGAR,
                                    NumberUtils.parseNumber(correctionInput.getText().toString())) :
                            PreferenceHelper.getInstance().getCorrectionValue();
            float meal = foodInputView.getTotalCarbohydrates();
            float factor = 0;
            if (meal > 0) {
                if (Validator.containsNumber(factorInput.getText().toString())) {
                    factor = NumberUtils.parseNumber(factorInput.getText().toString());
                } else if (Validator.containsNumber(factorInput.getHint().toString())) {
                    factor = NumberUtils.parseNumber(factorInput.getHint().toString());
                }
            }

            float insulinBolus = (meal * factor) / 10;
            float insulinCorrection = (currentBloodSugar - targetBloodSugar) / correction;

            StringBuilder builderFormula = new StringBuilder();
            builderFormula.append(String.format("%s = ", getString(R.string.bolus)));

            StringBuilder builderFormulaContent = new StringBuilder();
            builderFormulaContent.append(String.format("%s = ", getString(R.string.bolus)));

            if (insulinBolus > 0) {
                String carbohydrateAcronym = getResources().getStringArray(R.array.meal_units_acronyms)[1];
                builderFormula.append(String.format("%s * %s",
                        carbohydrateAcronym,
                        getString(R.string.factor)));
                builderFormula.append(" + ");

                builderFormulaContent.append(String.format("%s %s * %s",
                        Helper.parseFloat(meal / 10),
                        carbohydrateAcronym,
                        factor));
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

            showResult(builderFormula.toString(), builderFormulaContent.toString(),
                    currentBloodSugar, meal, insulinBolus, insulinCorrection);
        }
    }

    // Values are normalized
    private void showResult(String formula, String formulaContent, final float bloodSugar, final float meal, final float bolus, final float correction) {

        float insulin = bolus + correction;

        // Build AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View viewPopup = inflater.inflate(R.layout.dialog_calculator_result, null);

        final ViewGroup infoLayout = (ViewGroup) viewPopup.findViewById(R.id.dialog_calculator_result_info);

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
                .setNegativeButton(R.string.info, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Is set down below
                    }
                })
                .setPositiveButton(R.string.store_values, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        storeValues(bloodSugar, meal, bolus, correction);
                        finish();
                    }
                })
                .setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoLayout.setVisibility(View.VISIBLE);
                view.setEnabled(false);
            }
        });
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
            meal.setCarbohydrates(PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                    Measurement.Category.MEAL,
                    foodInputView.getInputCarbohydrates()));
            meal.setEntry(entry);
            MeasurementDao.getInstance(Meal.class).createOrUpdate(meal);

            for (FoodEaten foodEaten : foodInputView.getFoodEatenList()) {
                foodEaten.setMeal(meal);
                FoodEatenDao.getInstance().createOrUpdate(foodEaten);
            }
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
