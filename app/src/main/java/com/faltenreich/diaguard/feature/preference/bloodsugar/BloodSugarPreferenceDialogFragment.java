package com.faltenreich.diaguard.feature.preference.bloodsugar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreferenceDialogFragmentCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.validation.Validator;
import com.google.android.material.textfield.TextInputLayout;

// TODO: Style like BaseDialogFragment, e.g. TagEditFragment
public class BloodSugarPreferenceDialogFragment extends EditTextPreferenceDialogFragmentCompat {

    private final PreferenceStore preferenceStore = PreferenceStore.getInstance();

    @NonNull
    public static BloodSugarPreferenceDialogFragment newInstance(String key) {
        BloodSugarPreferenceDialogFragment fragment = new BloodSugarPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, key);
        fragment.setArguments(arguments);
        return fragment;
    }

    private TextInputLayout textInputLayout;
    private EditText editText;

    private BloodSugarPreferenceDialogFragment() {
        super();
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        bindViews(view);
        initLayout();
    }

    private void bindViews(View view) {
        textInputLayout = view.findViewById(R.id.text_input_layout);
        editText = view.findViewById(android.R.id.edit);
    }

    private void initLayout() {
        BloodSugarPreference preference = (BloodSugarPreference) getPreference();
        editText.setText(preference.getValueForUi());
        editText.setSelection(editText.getText() != null ? editText.getText().length() : 0);
        textInputLayout.setSuffixText(preferenceStore.getUnitName(Category.BLOODSUGAR));
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
                    textInputLayout,
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
        if (positiveResult && editText.getText() != null) {
            BloodSugarPreference preference = (BloodSugarPreference) getPreference();
            preference.setValueFromUi(editText.getText().toString());
        }
    }
}