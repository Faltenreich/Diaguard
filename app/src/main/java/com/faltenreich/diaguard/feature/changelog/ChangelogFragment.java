package com.faltenreich.diaguard.feature.changelog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.faltenreich.diaguard.R;

public class ChangelogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(requireContext())
            .setTitle(R.string.changelog)
            .setMessage(R.string.changelog_desc)
            .setPositiveButton(R.string.ok, (dlg, which) -> { })
            .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
