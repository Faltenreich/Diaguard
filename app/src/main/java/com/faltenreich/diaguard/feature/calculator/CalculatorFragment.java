package com.faltenreich.diaguard.feature.calculator;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.feature.food.input.FoodInputView;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.validation.Validator;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.preference.BloodSugarPreferenceChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.FactorChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.MealFactorUnitChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 10.09.2016.
 */
public class CalculatorFragment extends BaseFragment implements ToolbarDescribing, MainButton {

    @BindView(R.id.calculator_bloodsugar)
    StickyHintInput bloodSugarInput;
    @BindView(R.id.calculator_target)
    StickyHintInput targetInput;
    @BindView(R.id.calculator_correction)
    StickyHintInput correctionInput;
    @BindView(R.id.calculator_food_list_view)
    FoodInputView foodInputView;
    @BindView(R.id.calculator_factor)
    StickyHintInput factorInput;

    public CalculatorFragment() {
        super(R.layout.fragment_calculator);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.calculator)
            .build();
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
        targetInput.setText(PreferenceStore.getInstance().getMeasurementForUi(
            Category.BLOODSUGAR,
            PreferenceStore.getInstance().getTargetValue()));
    }

    private void updateCorrectionValue() {
        correctionInput.setText(PreferenceStore.getInstance().getMeasurementForUi(
            Category.BLOODSUGAR,
            PreferenceStore.getInstance().getCorrectionFactorForHour(
                DateTime.now().getHourOfDay())));
    }

    private void updateMealFactor() {
        int hourOfDay = DateTime.now().getHourOfDay();
        float mealFactor = PreferenceStore.getInstance().getMealFactorForHour(hourOfDay);
        factorInput.setText(mealFactor >= 0 ? FloatUtils.parseFloat(mealFactor) : null);
        factorInput.setHint(getString(PreferenceStore.getInstance().getMealFactorUnit().titleResId));
    }

    private boolean inputIsValid() {
        boolean isValid = true;

        // Blood Sugar
        if (!Validator.validateEditTextEvent(getContext(), bloodSugarInput.getEditText(), Category.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(getContext(), targetInput.getEditText(), Category.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validator.validateEditTextEvent(getContext(), correctionInput.getEditText(), Category.BLOODSUGAR, false)) {
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

    private float getBloodSugar() {
        return PreferenceStore.getInstance().formatCustomToDefaultUnit(
            Category.BLOODSUGAR,
            FloatUtils.parseNumber(bloodSugarInput.getText()));
    }

    private float getTargetBloodSugar() {
        return Validator.containsNumber(targetInput.getText()) ?
            PreferenceStore.getInstance().formatCustomToDefaultUnit(
                Category.BLOODSUGAR,
                FloatUtils.parseNumber(targetInput.getText())) :
            PreferenceStore.getInstance().getTargetValue();
    }

    private float getCorrectionFactor() {
        int hourOfDay = DateTime.now().getHourOfDay();
        return Validator.containsNumber(correctionInput.getText()) ?
            PreferenceStore.getInstance().formatCustomToDefaultUnit(
                Category.BLOODSUGAR,
                FloatUtils.parseNumber(correctionInput.getText())) :
            PreferenceStore.getInstance().getCorrectionFactorForHour(hourOfDay);
    }

    private float getCarbohydrates() {
        return foodInputView.getTotalCarbohydrates();
    }

    private float getMealFactor() {
        return Validator.containsNumber(factorInput.getText()) ?
            FloatUtils.parseNumber(factorInput.getText()) :
            FloatUtils.parseNumber(factorInput.getHint());
    }

    private void calculate() {
        if (inputIsValid()) {

            float bloodSugar = getBloodSugar();
            float targetBloodSugar = getTargetBloodSugar();
            float correctionFactor = getCorrectionFactor();
            float insulinCorrection = (bloodSugar - targetBloodSugar) / correctionFactor;

            float carbohydrates = getCarbohydrates();
            float mealFactor = getMealFactor();
            float insulinBolus = carbohydrates * mealFactor * PreferenceStore.getInstance().getMealFactorUnit().factor;

            StringBuilder builderFormula = new StringBuilder();
            StringBuilder builderFormulaContent = new StringBuilder();

            if (insulinBolus > 0) {
                String mealAcronym = PreferenceStore.getInstance().getUnitAcronym(Category.MEAL);
                String factorAcronym = getString(PreferenceStore.getInstance().getMealFactorUnit().titleResId);
                builderFormula.append(String.format("%s * %s",
                    mealAcronym,
                    factorAcronym));
                builderFormula.append(" + ");

                builderFormulaContent.append(String.format("%s %s * %s",
                    PreferenceStore.getInstance().getMeasurementForUi(Category.MEAL, carbohydrates),
                    mealAcronym,
                    FloatUtils.parseFloat(mealFactor)));
                builderFormulaContent.append(" + ");
            }

            builderFormula.append(String.format("(%s - %s) / %s",
                getString(R.string.bloodsugar),
                getString(R.string.pref_therapy_targets_target),
                getString(R.string.correction_value)));

            String bloodSugarUnit = PreferenceStore.getInstance().getUnitAcronym(Category.BLOODSUGAR);
            builderFormulaContent.append(String.format("(%s %s - %s %s) / %s %s",
                PreferenceStore.getInstance().getMeasurementForUi(Category.BLOODSUGAR, bloodSugar), bloodSugarUnit,
                PreferenceStore.getInstance().getMeasurementForUi(Category.BLOODSUGAR, targetBloodSugar), bloodSugarUnit,
                PreferenceStore.getInstance().getMeasurementForUi(Category.BLOODSUGAR, correctionFactor), bloodSugarUnit));

            builderFormula.append(String.format(" = %s", getString(R.string.bolus)));
            builderFormulaContent.append(String.format(" = %s %s",
                FloatUtils.parseFloat(insulinBolus + insulinCorrection),
                PreferenceStore.getInstance().getUnitAcronym(Category.INSULIN)));

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

        final ViewGroup infoLayout = viewPopup.findViewById(R.id.dialog_calculator_result_info);

        TextView textViewFormula = viewPopup.findViewById(R.id.dialog_calculator_result_formula);
        textViewFormula.setText(formula);

        TextView textViewFormulaContent = viewPopup.findViewById(R.id.dialog_calculator_result_formula_content);
        textViewFormulaContent.setText(formulaContent);

        // Handle negative insulin
        TextView textViewInfo = viewPopup.findViewById(R.id.textViewInfo);
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

        TextView textViewValue = viewPopup.findViewById(R.id.textViewResult);
        textViewValue.setText(FloatUtils.parseFloat(insulin));

        TextView textViewUnit = viewPopup.findViewById(R.id.textViewUnit);
        textViewUnit.setText(PreferenceStore.getInstance().getUnitAcronym(Category.INSULIN));

        dialogBuilder.setView(viewPopup)
            .setTitle(R.string.bolus)
            .setNegativeButton(R.string.info, (dialog, id) -> { /* Set down below */ })
            .setPositiveButton(R.string.store_values, (dialog, id) -> storeValues(bloodSugar, meal, bolus, correction))
            .setNeutralButton(R.string.back, (dialog, id) -> dialog.cancel());

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> {
            infoLayout.setVisibility(View.VISIBLE);
            view.setEnabled(false);
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

        List<FoodEaten> foodEatenList = new ArrayList<>();
        if (carbohydrates > 0) {
            foodEatenList.addAll(foodInputView.getFoodEatenList());
            Meal meal = new Meal();
            meal.setCarbohydrates(foodInputView.getInputCarbohydrates());
            meal.setEntry(entry);
            MeasurementDao.getInstance(Meal.class).createOrUpdate(meal);

            for (FoodEaten foodEaten : foodEatenList) {
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

        Events.post(new EntryAddedEvent(entry, null, foodEatenList));

        openEntry(entry);
        clearInput();
        update();
    }

    private void openEntry(Entry entry) {
        EntryEditActivity.show(getContext(), entry);
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.confirmButton(v -> calculate(), false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BloodSugarPreferenceChangedEvent event) {
        if (isAdded()) {
            updateTargetValue();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FactorChangedEvent event) {
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
        if (isAdded() && event.context == Category.BLOODSUGAR) {
            updateTargetValue();
            updateCorrectionValue();
        }
    }
}
