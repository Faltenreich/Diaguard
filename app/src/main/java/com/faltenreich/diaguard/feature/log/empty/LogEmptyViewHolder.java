package com.faltenreich.diaguard.feature.log.empty;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemLogEmptyBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditIntentFactory;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogEmptyViewHolder extends BaseViewHolder<ListItemLogEmptyBinding, LogEmptyListItem> {

    public LogEmptyViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_log_empty);
        getBinding().empty.setOnClickListener((view) -> createEntryForDate());
    }

    @Override
    protected ListItemLogEmptyBinding createBinding(View view) {
        return ListItemLogEmptyBinding.bind(view);
    }

    @Override
    public void onBind(LogEmptyListItem item) {
    }

    private void createEntryForDate() {
        DateTime now = DateTime.now();
        DateTime dateTime = getItem().getDateTime()
            .withHourOfDay(now.hourOfDay().get())
            .withMinuteOfHour(now.minuteOfHour().get())
            .withSecondOfMinute(now.secondOfMinute().get())
            .withMillisOfSecond(now.millisOfSecond().get());
        Intent intent = EntryEditIntentFactory.newInstance(getContext(), dateTime);
        getContext().startActivity(intent);
    }
}
