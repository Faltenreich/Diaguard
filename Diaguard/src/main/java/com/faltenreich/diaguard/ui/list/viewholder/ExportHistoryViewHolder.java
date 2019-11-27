package com.faltenreich.diaguard.ui.list.viewholder;

import android.content.ActivityNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.event.Events;
import com.faltenreich.diaguard.data.event.file.ExportHistoryDeleteEvent;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.ViewUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import java.io.File;

import butterknife.BindView;

public class ExportHistoryViewHolder extends BaseViewHolder<ListItemExportHistory> {

    private static final String TAG = ExportHistoryViewHolder.class.getSimpleName();

    @BindView(R.id.root_layout) ViewGroup rootLayout;
    @BindView(R.id.interval_label) TextView intervalLabel;
    @BindView(R.id.created_at_label) TextView createdAtLabel;
    @BindView(R.id.more_button) View moreButton;

    public ExportHistoryViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        ListItemExportHistory item = getListItem();

        Interval interval = item.getInterval();
        if (interval != null) {
            DateTime start = interval.getStart();
            DateTime end = interval.getEnd();
            String startString = DateTimeFormat.mediumDate().print(start);
            String endString = DateTimeFormat.mediumDate().print(end);
            intervalLabel.setText(String.format("%s - %s", startString, endString));
        } else {
            intervalLabel.setText(getContext().getString(R.string.export));
        }

        DateTime createdAt = item.getCreatedAt();
        if (createdAt != null) {
            String createdAtString = DateTimeFormat.mediumDateTime().print(createdAt);
            createdAtLabel.setText(getContext().getString(R.string.export_history_stamp, createdAtString));
            createdAtLabel.setVisibility(View.VISIBLE);
        } else {
            createdAtLabel.setText(null);
            createdAtLabel.setVisibility(View.GONE);
        }

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
        File file = getListItem().getFile();
        try {
            FileUtils.openFile(file, PdfExportConfig.MIME_TYPE, getContext());
        } catch (ActivityNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
            ViewUtils.showSnackbar(getView(), getContext().getString(R.string.error_no_app));
        }
    }

    private void shareExport() {
        FileUtils.shareFile(getContext(), getListItem().getFile(), PdfExportConfig.MIME_TYPE, R.string.export_share);
    }

    private void deleteExport() {
        Events.post(new ExportHistoryDeleteEvent(getListItem()));
    }
}
