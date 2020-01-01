package com.faltenreich.diaguard.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.event.Events;
import com.faltenreich.diaguard.data.event.file.ExportHistoryDeleteEvent;
import com.faltenreich.diaguard.data.event.permission.PermissionRequestEvent;
import com.faltenreich.diaguard.data.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.export.Export;
import com.faltenreich.diaguard.ui.list.adapter.ExportHistoryListAdapter;
import com.faltenreich.diaguard.ui.list.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ExportHistoryFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView listView;
    @BindView(R.id.progressView) View progressView;

    private ExportHistoryListAdapter listAdapter;

    public ExportHistoryFragment() {
        super(R.layout.fragment_export_history, R.string.export_history);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Events.register(this);
        initLayout();
        checkPermissions();
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    private void init() {
        listAdapter = new ExportHistoryListAdapter(getContext());
    }

    private void initLayout() {
        listView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        listView.addItemDecoration(new LinearDividerItemDecoration(getContext()));
        listView.setAdapter(listAdapter);
        progressView.setVisibility(View.VISIBLE);
    }

    private void checkPermissions() {
        Events.post(
            new PermissionRequestEvent(
                Permission.WRITE_EXTERNAL_STORAGE,
                PermissionUseCase.EXPORT_HISTORY
            )
        );
    }

    private void fetchHistory() {
        new FetchHistoryTask(getContext(), this::setHistory).execute();
    }

    private void setHistory(List<ListItemExportHistory> listItems) {
        listAdapter.clear();
        listAdapter.addItems(listItems);
        listAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.GONE);
    }

    private void deleteExportIfConfirmed(ListItemExportHistory item) {
        if (getContext() != null) {
            new AlertDialog.Builder(getContext())
                .setTitle(R.string.export_delete)
                .setMessage(R.string.export_delete_desc)
                .setNegativeButton(R.string.cancel, (dialog, which) -> { })
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteExport(item))
                .create()
                .show();
        }
    }

    private void deleteExport(ListItemExportHistory item) {
        // Runtime permission should be granted here
        FileUtils.deleteFile(item.getFile());

        int position = listAdapter.getItemPosition(item);
        listAdapter.removeItem(position);
        listAdapter.notifyItemRemoved(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE &&
            event.useCase == PermissionUseCase.EXPORT_HISTORY &&
            event.isGranted
        ) {
            fetchHistory();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ExportHistoryDeleteEvent event) {
        deleteExportIfConfirmed(event.context);
    }

    private static class FetchHistoryTask extends BaseAsyncTask<Void, Void, List<ListItemExportHistory>> {

        FetchHistoryTask(Context context, OnAsyncProgressListener<List<ListItemExportHistory>> onAsyncProgressListener) {
            super(context, onAsyncProgressListener);
        }

        @Override
        protected List<ListItemExportHistory> doInBackground(Void... voids) {
            File directory = FileUtils.getPublicDirectory();
            File[] files = directory.listFiles();

            List<ListItemExportHistory> listItems = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    ListItemExportHistory listItem = Export.getExportItem(file);
                    if (listItem != null) {
                        listItems.add(Export.getExportItem(file));
                    }
                }
            }

            Collections.sort(listItems, (first, second) ->
                second.getCreatedAt().compareTo(first.getCreatedAt())
            );

            return listItems;
        }
    }
}
