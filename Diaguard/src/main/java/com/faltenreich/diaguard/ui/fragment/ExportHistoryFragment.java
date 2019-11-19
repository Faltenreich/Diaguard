package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.event.Events;
import com.faltenreich.diaguard.data.event.PermissionRequestEvent;
import com.faltenreich.diaguard.data.event.PermissionResponseEvent;
import com.faltenreich.diaguard.export.Export;
import com.faltenreich.diaguard.ui.list.adapter.ExportHistoryListAdapter;
import com.faltenreich.diaguard.ui.list.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ExportHistoryFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView listView;

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
    }

    private void checkPermissions() {
        Events.post(new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, PermissionUseCase.EXPORT_HISTORY));
    }

    private void fetchHistory() {
        File directory = FileUtils.getPublicDirectory();
        File[] files = directory.listFiles();
        List<ListItemExportHistory> listItems = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                DateTime dateTime = Export.getExportDateTime(getContext(), file);
                if (dateTime == null) {
                    dateTime = DateTime.now();
                }
                listItems.add(new ListItemExportHistory(file, dateTime));
            }
        }

        Collections.sort(listItems, (first, second) -> second.getCreatedAt().compareTo(first.getCreatedAt()));

        setHistory(listItems);
    }

    private void setHistory(List<ListItemExportHistory> listItems) {
        listAdapter.clear();
        listAdapter.addItems(listItems);
        listAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE && event.useCase == PermissionUseCase.EXPORT_HISTORY && event.isGranted) {
            fetchHistory();
        }
    }
}
