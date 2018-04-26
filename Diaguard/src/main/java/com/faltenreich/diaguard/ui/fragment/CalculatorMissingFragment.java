package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.WebUtils;

public class CalculatorMissingFragment extends DialogFragment {

    // TODO: Update url when multi-language blog post is out
    private static final String URL = "https://diaguard.wordpress.com/2018/04/26/diaguard-ohne-bolusrechner";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.calculator_missing)
                .setMessage(R.string.calculator_missing_desc)
                .setNegativeButton(R.string.calculator_more_info, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebUtils.openUri(getContext(), Uri.parse(URL));
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss
                    }
                })
                .create();
    }
}
