package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.activity.CategoriesActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ChangelogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.changelog)
                .setMessage(R.string.changelog_desc)
                .setNegativeButton(R.string.change_now, (dlg, which) -> openCategoryPreference())
                .setPositiveButton(R.string.ok, (dlg, which) -> { })
                .create();
    }

    private void openCategoryPreference() {
        if (getContext() != null) {
            getContext().startActivity(new Intent(getContext(), CategoriesActivity.class));
        }
    }
}
