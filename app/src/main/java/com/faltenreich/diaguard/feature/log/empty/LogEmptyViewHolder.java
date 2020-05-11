package com.faltenreich.diaguard.feature.log.empty;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogEmptyViewHolder extends BaseViewHolder<LogEmptyListItem> implements View.OnClickListener {

    @BindView(R.id.empty) TextView textView;

    public LogEmptyViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_log_empty);
    }

    @Override
    public void onBind(LogEmptyListItem item) {
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DateTime now = DateTime.now();
        DateTime dateTime = getItem().getDateTime()
                .withHourOfDay(now.hourOfDay().get())
                .withMinuteOfHour(now.minuteOfHour().get())
                .withSecondOfMinute(now.secondOfMinute().get())
                .withMillisOfSecond(now.millisOfSecond().get());
        EntryEditActivity.show(getContext(), dateTime);
    }
}
