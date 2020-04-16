package com.faltenreich.diaguard.feature.preference.bloodsugar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.preference.EditTextPreferenceDialogFragmentCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.validation.Validator;
import com.faltenreich.diaguard.shared.view.edittext.LocalizedNumberEditText;

public class BloodSugarPreferenceDialogFragment extends EditTextPreferenceDialogFragmentCompat {

    public static BloodSugarPreferenceDialogFragment newInstance(String key) {
        BloodSugarPreferenceDialogFragment fragment = new BloodSugarPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, key);
        fragment.setArguments(arguments);
        return fragment;
    }

    private LocalizedNumberEditText editTextValue;
    private TextView textViewUnit;

    private BloodSugarPreferenceDialogFragment() {
        super();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        bindViews(view);
        initLayout();
    }

    private void bindViews(View view) {
        editTextValue = view.findViewById(android.R.id.edit);
        textViewUnit = view.findViewById(R.id.unit);
    }

    private void initLayout() {
        BloodSugarPreference preference = (BloodSugarPreference) getPreference();

        editTextValue.setText(preference.getValueForUi());
        editTextValue.setSelection(editTextValue.getText() != null ? editTextValue.getText().length() : 0);

        textViewUnit.setText(PreferenceHelper.getInstance().getUnitName(Category.BLOODSUGAR));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        boolean isValid = true;
        if (which == DialogInterface.BUTTON_POSITIVE) {
            isValid = Validator.validateEditTextEvent(
                getContext(),
                editTextValue,
                Category.BLOODSUGAR,
                true
            );
        }
        if (isValid) {
            super.onClick(dialog, which);
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult && editTextValue != null && editTextValue.getText() != null) {
            BloodSugarPreference preference = (BloodSugarPreference) getPreference();
            preference.setValueFromUi(editTextValue.getText().toString());
        }
    }
}