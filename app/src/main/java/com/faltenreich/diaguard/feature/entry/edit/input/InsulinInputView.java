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
        bolusInputField.setSuffixText(PreferenceStore.getInstance().getUnitAcronym(Category.INSULIN));
        EditTextUtils.afterTextChanged(bolusInputField.getEditText(), () -> {
            measurement.setBolus(bolusInputField.getText().length() > 0 ?
                PreferenceStore.getInstance().formatCustomToDefaultUnit(
                    measurement.getCategory(),
                    FloatUtils.parseNumber(bolusInputField.getText())
                ) : 0);
        });

        correctionInputField.setText(measurement.getValuesForUI()[1]);
        correctionInputField.setSuffixText(PreferenceStore.getInstance().getUnitAcronym(Category.INSULIN));
        EditTextUtils.afterTextChanged(correctionInputField.getEditText(), () -> {
            measurement.setCorrection(correctionInputField.getText().length() > 0 ?
                PreferenceStore.getInstance().formatCustomToDefaultUnit(
                    measurement.getCategory(),
                    FloatUtils.parseNumber(correctionInputField.getText())
                ) : 0);
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
        basalInputField.setSuffixText(PreferenceStore.getInstance().getUnitAcronym(Category.INSULIN));
        EditTextUtils.afterTextChanged(basalInputField.getEditText(), () -> {
            measurement.setBasal(basalInputField.getText().length() > 0 ?
                PreferenceStore.getInstance().formatCustomToDefaultUnit(
                    measurement.getCategory(),
                    FloatUtils.parseNumber(basalInputField.getText())
                ) : 0);
        });
    }

    @Override
    public boolean isValid() {
        String bolus = bolusInputField.getText().trim();
        String correction = correctionInputField.getText().trim();
        String basal = basalInputField.getText().trim();

        if (StringUtils.isBlank(bolus) && StringUtils.isBlank(correction) && StringUtils.isBlank(basal)) {
            return true;
        }

        boolean isValid = true;
        if (!StringUtils.isBlank(bolus)) {
            isValid = PreferenceStore.getInstance().isValueValid(bolusInputField.getEditText(), Category.INSULIN);
        }
        if (!StringUtils.isBlank(correction)) {
            isValid = PreferenceStore.getInstance().isValueValid(correctionInputField.getEditText(), Category.INSULIN, true);
        }
        if (!StringUtils.isBlank(basal)) {
            isValid = PreferenceStore.getInstance().isValueValid(basalInputField.getEditText(), Category.INSULIN);
        }
        return isValid;
    }
}