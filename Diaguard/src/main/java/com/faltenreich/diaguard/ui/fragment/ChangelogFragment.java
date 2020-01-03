package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.activity.MainActivity;

public class ChangelogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(requireContext())
            .setTitle(R.string.changelog)
            .setMessage(R.string.changelog_desc)
            .setNegativeButton(R.string.export_open, (dlg, which) -> {
                dismiss();
                if (getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.showFragment(new ExportFragment(), null, true);
                }
            })
            .setPositiveButton(R.string.ok, (dlg, which) -> { })
            .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
