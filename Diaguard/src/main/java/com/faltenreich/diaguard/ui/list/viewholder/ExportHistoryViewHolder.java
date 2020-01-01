package com.faltenreich.diaguard.ui.list.viewholder;

import android.content.ActivityNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.event.Events;
import com.faltenreich.diaguard.data.event.file.ExportHistoryDeleteEvent;
import com.faltenreich.diaguard.export.FileType;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.ViewUtils;

import org.joda.time.format.DateTimeFormat;

import java.io.File;

import butterknife.BindView;

public class ExportHistoryViewHolder extends BaseViewHolder<ListItemExportHistory> {

    private static final String TAG = ExportHistoryViewHolder.class.getSimpleName();

    @BindView(R.id.root_layout) ViewGroup rootLayout;
    @BindView(R.id.format_icon) ImageView formatIcon;
    @BindView(R.id.format_label) TextView formatLabel;
    @BindView(R.id.created_at_label) TextView createdAtLabel;
    @BindView(R.id.more_button) View moreButton;

    public ExportHistoryViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        ListItemExportHistory item = getListItem();

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
            File file = getListItem().getFile();
            FileUtils.openFile(getContext(), file);
        } catch (ActivityNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
            ViewUtils.showSnackbar(getView(), getContext().getString(R.string.error_no_app));
        }
    }

    private void shareExport() {
        FileUtils.shareFile(getContext(), getListItem().getFile(), R.string.export_share);
    }

    private void deleteExport() {
        Events.post(new ExportHistoryDeleteEvent(getListItem()));
    }
}
