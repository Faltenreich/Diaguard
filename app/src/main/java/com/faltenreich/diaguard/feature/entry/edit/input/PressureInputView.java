package com.faltenreich.diaguard.feature.entry.edit.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;

import com.faltenreich.diaguard.databinding.ListItemMeasurementPressureBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.view.edittext.EditTextUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
@SuppressLint("ViewConstructor")
public class PressureInputView extends MeasurementInputView<ListItemMeasurementPressureBinding, Pressure> {

    private final PreferenceStore preferenceStore = PreferenceStore.getInstance();

    private final StickyHintInputView systolicInputField;
    private final StickyHintInputView diastolicInputField;

    public PressureInputView(Context context, Pressure pressure) {
        super(context, Pressure.class, pressure);
        systolicInputField = getBinding().systolicInputField;
        systolicInputField.getEditText().setSaveEnabled(false);
        diastolicInputField = getBinding().diastolicInputField;
        diastolicInputField.getEditText().setSaveEnabled(false);
    }

    @Override
    protected ListItemMeasurementPressureBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementPressureBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onBind(Pressure measurement) {
        systolicInputField.setText(measurement.getValuesForUI()[0]);
        systolicInputField.setSuffixText(preferenceStore.getUnitAcronym(Category.PRESSURE));
        EditTextUtils.afterTextChanged(systolicInputField.getEditText(), () ->
            measurement.setSystolic(preferenceStore.formatCustomToDefaultUnit(
                measurement.getCategory(),
                systolicInputField.getText() != null ? FloatUtils.parseNumber(systolicInputField.getText()) : 0))
        );
        // Workaround: Fixing imeOptions for TextInputLayout in horizontal LinearLayout
        systolicInputField.getEditText().setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                diastolicInputField.getEditText().requestFocus();
                return true;
            }
            return false;
        });

        diastolicInputField.setText(measurement.getValuesForUI()[1]);
        diastolicInputField.setSuffixText(preferenceStore.getUnitAcronym(Category.PRESSURE));
        EditTextUtils.afterTextChanged(diastolicInputField.getEditText(), () ->
            measurement.setDiastolic(preferenceStore.formatCustomToDefaultUnit(
                measurement.getCategory(),
                diastolicInputField.getText() != null ? FloatUtils.parseNumber(diastolicInputField.getText()) : 0))
        );
    }

    @Override
    public boolean hasInput() {
        return !StringUtils.isBlank(systolicInputField.getText())
            && !StringUtils.isBlank(diastolicInputField.getText());
    }

    @Override
    public boolean isValid() {
        return preferenceStore.isValueValid(systolicInputField.getEditText(), Category.PRESSURE)
            && preferenceStore.isValueValid(diastolicInputField.getEditText(), Category.PRESSURE);
    }
}