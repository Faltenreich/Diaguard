package com.faltenreich.diaguard.feature.export.history;

import android.content.ActivityNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemExportHistoryBinding;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.ExportHistoryDeleteEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.format.DateTimeFormat;

import java.io.File;

class ExportHistoryViewHolder extends BaseViewHolder<ListItemExportHistoryBinding, ExportHistoryListItem> {

    private static final String TAG = ExportHistoryViewHolder.class.getSimpleName();

    ExportHistoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_export_history);
    }

    @Override
    protected ListItemExportHistoryBinding createBinding(View view) {
        return ListItemExportHistoryBinding.bind(view);
    }

    @Override
    protected void onBind(ExportHistoryListItem item) {
        FileType format = FileType.valueOf(item.getFile());
        getBinding().formatIcon.setColorFilter(ContextCompat.getColor(getContext(), format != null ? format.colorRes : R.color.gray));
        getBinding().formatLabel.setText(format != null ? format.extension : null);

        getBinding().createdAtLabel.setText(DateTimeFormat.mediumDateTime().print(item.getCreatedAt()));

        getBinding().container.setOnClickListener(view -> openExport());
        getBinding().moreButton.setOnClickListener(this::openMenu);
    }

    private void openMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.export_history_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_open) {
                openExport();
            } else if (itemId == R.id.action_share) {
                shareExport();
            } else if (itemId == R.id.action_delete) {
                deleteExport();
            }
            return true;
        });
        popupMenu.show();
    }

    private void openExport() {
        try {
            File file = getItem().getFile();
            FileUtils.openFile(getContext(), file);
        } catch (ActivityNotFoundException exception) {
            Log.e(TAG, exception.toString());
            ViewUtils.showSnackbar(itemView, getContext().getString(R.string.error_no_app));
        }
    }

    private void shareExport() {
        FileUtils.shareFile(getContext(), getItem().getFile(), R.string.export_share);
    }

    private void deleteExport() {
        Events.post(new ExportHistoryDeleteEvent(getItem()));
    }
}
