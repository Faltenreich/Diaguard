package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.WebUtils;

public class CalculatorMissingFragment extends DialogFragment {
    private static final String URL = "https://diaguard.wordpress.com/2018/05/01/diaguard-2-4-3";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.calculator_missing)
                .setMessage(R.string.calculator_missing_desc)
                .setNegativeButton(R.string.calculator_more_info, (dialog1, which) -> WebUtils.openUri(getContext(), Uri.parse(URL)))
                .setPositiveButton(R.string.ok, (dialog12, which) -> {})
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
