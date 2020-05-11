package com.faltenreich.diaguard.feature.export.history;

import android.content.ActivityNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.ExportHistoryDeleteEvent;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import org.joda.time.format.DateTimeFormat;

import java.io.File;

import butterknife.BindView;

class ExportHistoryViewHolder extends BaseViewHolder<ExportHistoryListItem> {

    private static final String TAG = ExportHistoryViewHolder.class.getSimpleName();

    @BindView(R.id.root_layout) ViewGroup rootLayout;
    @BindView(R.id.format_icon) ImageView formatIcon;
    @BindView(R.id.format_label) TextView formatLabel;
    @BindView(R.id.created_at_label) TextView createdAtLabel;
    @BindView(R.id.more_button) View moreButton;

    ExportHistoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_export_history);
    }

    @Override
    protected void bind() {
        ExportHistoryListItem item = getItem();

        FileType format = FileType.valueOf(item.getFile());
        if (format != null) {
            formatIcon.setColorFilter(ContextCompat.getColor(getContext(), format.colorRes));
            formatLabel.setText(format.extension);
        } else {
            formatIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray));
            formatLabel.setText(null);
        }

        createdAtLabel.setText(DateTimeFormat.mediumDateTime().print(item.getCreatedAt()));

        rootLayout.setOnClickListener(view -> openExport());
        moreButton.setOnClickListener(this::openMenu);
    }

    private void openMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.export_history_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_open:
                    openExport();
                    break;
                case R.id.action_share:
                    shareExport();
                    break;
                case R.id.action_delete:
                    deleteExport();
                    break;
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
            Log.e(TAG, exception.getMessage());
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
