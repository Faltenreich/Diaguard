package com.faltenreich.diaguard.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
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
import com.faltenreich.diaguard.event.preference.BloodSugarPreferenceChangedEvent;
import com.faltenreich.diaguard.event.preference.CorrectionFactorChangedEvent;
import com.faltenreich.diaguard.event.preference.MealFactorChangedEvent;
import com.faltenreich.diaguard.event.preference.MealFactorUnitChangedEvent;
import com.faltenreich.diaguard.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.view.FoodInputView;
import com.faltenreich.diaguard.ui.view.MainButton;
import com.faltenreich.diaguard.ui.view.MainButtonProperties;
import com.faltenreich.diaguard.ui.view.StickyHintInput;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.Validator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 10.09.2016.
 */
public class CalculatorFragment extends BaseFragment implements MainButton {

    @BindView(R.id.calculator_bloodsugar) StickyHintInput bloodSugarInput;
    @BindView(R.id.calculator_target) StickyHintInput targetInput;
    @BindView(R.id.calculator_correction) StickyHintInput correctionInput;
    @BindView(R.id.calculator_food_list_view) FoodInputView foodInputView;
    @BindView(R.id.calculator_factor) StickyHintInput factorInput;

    public CalculatorFragment() {
        super(R.layout.fragment_calculator, R.string.calculator, R.menu.main);
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
        update();
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    private void update() {
        updateTargetValue();
        updateCorrectionValue();
        updateMealFactor();
    }

    private void clearInput() {
        bloodSugarInput.setText(null);
        targetInput.setText(null);
        correctionInput.setText(null);
        foodInputView.clear();
        factorInput.setText(null);
    }

    private void updateTargetValue() {
        targetInput.setText(PreferenceHelper.getInstance().getMeasurementForUi(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getTargetValue()));
    }

    private void updateCorrectionValue() {
        correctionInput.setText(PreferenceHelper.getInstance().getMeasurementForUi(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getCorrectionForHour(
                        DateTime.now().getHourOfDay())));
    }

    private void updateMealFactor() {
        int hourOfDay = DateTime.now().getHourOfDay();
        float factor = PreferenceHelper.getInstance().getFactorForHour(hourOfDay);
        factorInput.setText(factor >= 0 ? Helper.parseFloat(factor) : null);
        factorInput.setHint(getString(PreferenceHelper.getInstance().getFactorUnit().titleResId));
    }

    private boolean inputIsValid() {
        boolean isValid = true;

        // Blood Sugar
        if (!Validator.validateEditTextEvent(getContext(), bloodSugarInput.getInputView(), Measurement.Category.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(getContext(), targetInput.getInputView(), Measurement.Category.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(getContext(), correctionInput.getInputView(), Measurement.Category.BLOODSUGAR, false)) {
            isValid = false;
        }

        // Meal
        if (foodInputView.getTotalCarbohydrates() > 0) {
            // Factor
            if (!Validator.validateEditTextFactor(getContext(), factorInput.getInputView(), false)) {
                isValid = false;
            } else {
                factorInput.setError(null);
            }
        }

        return isValid;
    }

    private float getBloodSugar() {
        return PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                Measurement.Category.BLOODSUGAR,
                NumberUtils.parseNumber(bloodSugarInput.getText()));
    }

    private float getTargetBloodSugar() {
        return Validator.containsNumber(targetInput.getText()) ?
                PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                        Measurement.Category.BLOODSUGAR,
                        NumberUtils.parseNumber(targetInput.getText())) :
                PreferenceHelper.getInstance().getTargetValue();
    }

    private float getCorrectionFactor() {
        int hourOfDay = DateTime.now().getHourOfDay();
        return Validator.containsNumber(correctionInput.getText()) ?
                PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                        Measurement.Category.BLOODSUGAR,
                        NumberUtils.parseNumber(correctionInput.getText())) :
                PreferenceHelper.getInstance().getCorrectionForHour(hourOfDay);
    }

    private float getCarbohydrates() {
        return foodInputView.getTotalCarbohydrates();
    }

    private float getMealFactor() {
        return Validator.containsNumber(factorInput.getText()) ?
                NumberUtils.parseNumber(factorInput.getText()) :
                NumberUtils.parseNumber(factorInput.getHint());
    }

    private void calculate() {
        if (inputIsValid()) {

            float bloodSugar = getBloodSugar();
            float targetBloodSugar = getTargetBloodSugar();
            float correctionFactor = getCorrectionFactor();
            float insulinCorrection = (bloodSugar - targetBloodSugar) / correctionFactor;

            float carbohydrates = getCarbohydrates();
            float mealFactor = getMealFactor();
            float insulinBolus = carbohydrates * mealFactor * PreferenceHelper.getInstance().getFactorUnit().factor;

            StringBuilder builderFormula = new StringBuilder();
            StringBuilder builderFormulaContent = new StringBuilder();

            if (insulinBolus > 0) {
                String mealAcronym = PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.MEAL);
                String factorAcronym = getString(PreferenceHelper.getInstance().getFactorUnit().titleResId);
                builderFormula.append(String.format("%s * %s",
                        mealAcronym,
                        factorAcronym));
                builderFormula.append(" + ");

                builderFormulaContent.append(String.format("%s %s * %s",
                        PreferenceHelper.getInstance().getMeasurementForUi(Measurement.Category.MEAL, carbohydrates),
                        mealAcronym,
                        Helper.parseFloat(mealFactor)));
                builderFormulaContent.append(" + ");
            }

            builderFormula.append(String.format("(%s - %s) / %s",
                    getString(R.string.bloodsugar),
                    getString(R.string.pref_therapy_targets_target),
                    getString(R.string.correction_value)));

            String bloodSugarUnit = PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR);
            builderFormulaContent.append(String.format("(%s %s - %s %s) / %s %s",
                    PreferenceHelper.getInstance().getMeasurementForUi(Measurement.Category.BLOODSUGAR, bloodSugar), bloodSugarUnit,
                    PreferenceHelper.getInstance().getMeasurementForUi(Measurement.Category.BLOODSUGAR, targetBloodSugar), bloodSugarUnit,
                    PreferenceHelper.getInstance().getMeasurementForUi(Measurement.Category.BLOODSUGAR, correctionFactor), bloodSugarUnit));

            builderFormula.append(String.format(" = %s", getString(R.string.bolus)));
            builderFormulaContent.append(String.format(" = %s %s",
                    Helper.parseFloat(insulinBolus + insulinCorrection),
                    PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.INSULIN)));

            showResult(builderFormula.toString(), builderFormulaContent.toString(), bloodSugar, carbohydrates, insulinBolus, insulinCorrection);
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
            meal.setCarbohydrates(foodInputView.getInputCarbohydrates());
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

        Events.post(new EntryAddedEvent(entry, null));

        openEntry(entry);
        clearInput();
        update();
    }

    private void openEntry(Entry entry) {
        EntryActivity.show(getContext(), entry);
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.confirmButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BloodSugarPreferenceChangedEvent event) {
        if (isAdded()) {
            updateTargetValue();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CorrectionFactorChangedEvent event) {
        if (isAdded()) {
            updateCorrectionValue();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MealFactorChangedEvent event) {
        if (isAdded()) {
            updateMealFactor();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MealFactorUnitChangedEvent event) {
        if (isAdded()) {
            updateMealFactor();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEvent event) {
        if (isAdded() && event.context != null && event.context == Measurement.Category.BLOODSUGAR) {
            updateTargetValue();
            updateCorrectionValue();
        }
    }
}
