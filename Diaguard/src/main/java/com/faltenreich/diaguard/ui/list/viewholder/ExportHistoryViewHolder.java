package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import java.io.File;

import butterknife.BindView;

public class ExportHistoryViewHolder extends BaseViewHolder<ListItemExportHistory> {

    @BindView(R.id.interval_label) TextView intervalLabel;
    @BindView(R.id.created_at_label) TextView createdAtLabel;

    public ExportHistoryViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        ListItemExportHistory item = getListItem();
        File file = item.getFile();

        Interval interval = item.getInterval();
        if (interval != null) {
            DateTime start = interval.getStart();
            DateTime end = interval.getEnd();
            String startString = DateTimeFormat.mediumDate().print(start);
            String endString = DateTimeFormat.mediumDate().print(end);
            intervalLabel.setText(String.format("%s - %s", startString, endString));
        } else {
            intervalLabel.setText(null);
            intervalLabel.setVisibility(View.GONE);
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
    }
}
