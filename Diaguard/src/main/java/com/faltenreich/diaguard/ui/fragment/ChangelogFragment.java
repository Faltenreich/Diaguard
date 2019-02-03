package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.ChangelogAdapter;
import com.faltenreich.diaguard.data.PreferenceHelper;

/**
 * Created by Faltenreich on 26.03.2017
 */

public class ChangelogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RecyclerView list = new RecyclerView(getContext());
        String[] changelog = PreferenceHelper.getInstance().getChangelog(getContext());
        ChangelogAdapter adapter = new ChangelogAdapter(getContext(), changelog);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.changelog)
                .setView(list)
                .setPositiveButton(R.string.ok, (dialog, which) -> {});
        return builder.create();
    }
}
