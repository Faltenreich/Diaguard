package com.faltenreich.diaguard.feature.export.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentExportHistoryBinding;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.data.async.BaseAsyncTask;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.ExportHistoryDeleteEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionRequestEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ExportHistoryFragment extends BaseFragment<FragmentExportHistoryBinding> implements ToolbarDescribing {

    @BindView(R.id.list) RecyclerView listView;
    @BindView(R.id.progressView) View progressView;

    private ExportHistoryListAdapter listAdapter;

    public ExportHistoryFragment() {
        super(R.layout.fragment_export_history);
    }

    @Override
    protected FragmentExportHistoryBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentExportHistoryBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.export_history)
            .build();
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
        listView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
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

    private void setHistory(List<ExportHistoryListItem> listItems) {
        listAdapter.clear();
        listAdapter.addItems(listItems);
        listAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.GONE);
    }

    private void deleteExportIfConfirmed(ExportHistoryListItem item) {
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

    private void deleteExport(ExportHistoryListItem item) {
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

    private static class FetchHistoryTask extends BaseAsyncTask<Void, Void, List<ExportHistoryListItem>> {

        FetchHistoryTask(Context context, OnAsyncProgressListener<List<ExportHistoryListItem>> onAsyncProgressListener) {
            super(context, onAsyncProgressListener);
        }

        @Override
        protected List<ExportHistoryListItem> doInBackground(Void... voids) {
            File directory = FileUtils.getPublicDirectory(getContext());
            File[] files = directory.listFiles();

            List<ExportHistoryListItem> listItems = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    ExportHistoryListItem listItem = Export.getExportItem(file);
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
