package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
                .setTitle(R.string.changelog_title)
                .setView(list)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss
                    }
                });
        return builder.create();
    }
}
