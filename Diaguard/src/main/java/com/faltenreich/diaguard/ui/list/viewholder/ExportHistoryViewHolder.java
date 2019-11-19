package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;

import java.io.File;

import butterknife.BindView;

public class ExportHistoryViewHolder extends BaseViewHolder<ListItemExportHistory> {

    @BindView(R.id.date_range_label) TextView dateRangeLabel;
    @BindView(R.id.created_at_label) TextView createdAtLabel;

    public ExportHistoryViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        ListItemExportHistory item = getListItem();
        File file = item.getFile();
        // TODO
        dateRangeLabel.setText(file.getName());
    }
}