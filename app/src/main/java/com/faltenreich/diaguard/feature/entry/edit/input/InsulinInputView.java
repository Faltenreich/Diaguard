package com.faltenreich.diaguard.feature.entry.edit.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;

import com.faltenreich.diaguard.databinding.ListItemMeasurementInsulinBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.view.edittext.EditTextUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
@SuppressLint("ViewConstructor")
public class InsulinInputView extends MeasurementInputView<ListItemMeasurementInsulinBinding, Insulin> {

    private final PreferenceStore preferenceStore = PreferenceStore.getInstance();

    private final StickyHintInputView bolusInputField;
    private final StickyHintInputView correctionInputField;
    private final StickyHintInputView basalInputField;

    public InsulinInputView(Context context, Insulin insulin) {
        super(context, Insulin.class, insulin);
        bolusInputField = getBinding().bolusInputField;
        bolusInputField.getEditText().setSaveEnabled(false);
        correctionInputField = getBinding().correctionInputField;
        correctionInputField.getEditText().setSaveEnabled(false);
        basalInputField = getBinding().basalInputField;
        basalInputField.getEditText().setSaveEnabled(false);
    }

    @Override
    protected ListItemMeasurementInsulinBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementInsulinBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onBind(Insulin measurement) {
        bolusInputField.setText(measurement.getValuesForUI()[0]);
        bolusInputField.setSuffixText(preferenceStore.getUnitAcronym(Category.INSULIN));
        EditTextUtils.afterTextChanged(bolusInputField.getEditText(), () -> {
            float value = bolusInputField.getText() != null && bolusInputField.getText().length() > 0
                ? preferenceStore.formatCustomToDefaultUnit(measurement.getCategory(), FloatUtils.parseNumber(bolusInputField.getText()))
                : 0;
            measurement.setBolus(value);
        });

        correctionInputField.setText(measurement.getValuesForUI()[1]);
        correctionInputField.setSuffixText(preferenceStore.getUnitAcronym(Category.INSULIN));
        EditTextUtils.afterTextChanged(correctionInputField.getEditText(), () -> {
            float value = correctionInputField.getText() != null && correctionInputField.getText().length() > 0
                ? preferenceStore.formatCustomToDefaultUnit(measurement.getCategory(), FloatUtils.parseNumber(correctionInputField.getText()))
                : 0;
            measurement.setCorrection(value);
        });
        // Workaround: Fixing imeOptions for TextInputLayout in horizontal LinearLayout
        correctionInputField.getEditText().setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                basalInputField.getEditText().requestFocus();
                return true;
            }
            return false;
        });

        basalInputField.setText(measurement.getValuesForUI()[2]);
        basalInputField.setSuffixText(preferenceStore.getUnitAcronym(Category.INSULIN));
        EditTextUtils.afterTextChanged(basalInputField.getEditText(), () -> {
            float value = basalInputField.getText() != null && basalInputField.getText().length() > 0
                ? preferenceStore.formatCustomToDefaultUnit(measurement.getCategory(), FloatUtils.parseNumber(basalInputField.getText()))
                : 0;
            measurement.setBasal(value);
        });
    }

    @Override
    public boolean hasInput() {
        return !StringUtils.isBlank(bolusInputField.getText())
            || !StringUtils.isBlank(correctionInputField.getText())
            || !StringUtils.isBlank(basalInputField.getText());
    }

    @Override
    public boolean isValid() {
        boolean isBolusValid = StringUtils.isBlank(bolusInputField.getText())
            || preferenceStore.isValueValid(bolusInputField.getEditText(), Category.INSULIN, false);
        boolean isCorrectionValid = StringUtils.isBlank(correctionInputField.getText())
            || preferenceStore.isValueValid(correctionInputField.getEditText(), Category.INSULIN, true);
        boolean isBasalValid = StringUtils.isBlank(basalInputField.getText())
            || preferenceStore.isValueValid(basalInputField.getEditText(), Category.INSULIN, false);
        return isBolusValid && isCorrectionValid && isBasalValid;
    }
}