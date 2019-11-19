package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.event.Events;
import com.faltenreich.diaguard.data.event.PermissionRequestEvent;
import com.faltenreich.diaguard.data.event.PermissionResponseEvent;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class ExportHistoryFragment extends BaseFragment {

    public ExportHistoryFragment() {
        super(R.layout.fragment_export_history, R.string.export_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    private void checkPermissions() {
        Events.post(new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, PermissionUseCase.EXPORT_HISTORY));
    }

    private void fetchHistory() {
        File directory = FileUtils.getPublicDirectory();
        File[] files = directory.listFiles();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE && event.useCase == PermissionUseCase.EXPORT_HISTORY && event.isGranted) {
            fetchHistory();
        }
    }
}
