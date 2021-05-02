package com.faltenreich.diaguard.feature.log.empty;

import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemLogEmptyBinding;
import com.faltenreich.diaguard.feature.log.LogListAdapter;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogEmptyViewHolder extends BaseViewHolder<ListItemLogEmptyBinding, LogEmptyListItem> {

    public LogEmptyViewHolder(ViewGroup parent, LogListAdapter.Listener listener) {
        super(parent, R.layout.list_item_log_empty);
        getBinding().empty.setOnClickListener((view) -> {
            DateTime now = DateTime.now();
            DateTime dateTime = getItem().getDateTime()
                .withHourOfDay(now.hourOfDay().get())
                .withMinuteOfHour(now.minuteOfHour().get())
                .withSecondOfMinute(now.secondOfMinute().get())
                .withMillisOfSecond(now.millisOfSecond().get());
            listener.onDateSelected(dateTime);
        });
    }

    @Override
    protected ListItemLogEmptyBinding createBinding(View view) {
        return ListItemLogEmptyBinding.bind(view);
    }

    @Override
    public void onBind(LogEmptyListItem item) {

    }
}
