package com.faltenreich.diaguard.feature.log.pending;

import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemLogPendingBinding;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogPendingViewHolder extends BaseViewHolder<ListItemLogPendingBinding, LogPendingListItem> {

    public LogPendingViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_log_pending);
    }

    @Override
    protected ListItemLogPendingBinding createBinding(View view) {
        return ListItemLogPendingBinding.bind(view);
    }

    @Override
    public void onBind(LogPendingListItem item) {

    }
}
