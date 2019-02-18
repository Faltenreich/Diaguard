package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.WebUtils;

/**
 * Created by Faltenreich on 26.03.2017
 */

public class ChangelogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_changelog, null);
        TextView textView = view.findViewById(R.id.changelog);
        String changelog = WebUtils.loadHtml(getContext(), R.raw.changelog);
        textView.setText(Html.fromHtml(changelog));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.changelog)
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {});
        return builder.create();
    }
}
