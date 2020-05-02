package com.faltenreich.diaguard.feature.preference.bloodsugar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(shownDialog -> {
            AlertDialog alertDialog = (AlertDialog) shownDialog;
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                // Close dialog only if input is valid, otherwise keep open
                boolean isValid = Validator.validateEditTextEvent(
                    getContext(),
                    editTextValue,
                    Category.BLOODSUGAR,
                    true
                );
                if (isValid) {
                    dismiss();
                    onDialogClosed(true);
                }
            });
        });
        return dialog;
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