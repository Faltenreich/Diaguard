package com.faltenreich.diaguard.feature.log.day;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemLogDayBinding;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogDayViewHolder extends BaseViewHolder<ListItemLogDayBinding, LogDayListItem> {

    public LogDayViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_log_day);
    }

    @Override
    protected ListItemLogDayBinding createBinding(View view) {
        return ListItemLogDayBinding.bind(view);
    }

    @Override
    public void onBind(LogDayListItem item) {
        DateTime dateTime = item.getDateTime();
        invalidateText(dateTime);
        invalidateTextColor(dateTime);
    }

    private void invalidateText(DateTime dateTime) {
        getBinding().dateLabel.setText(dateTime.toString("dd"));
        getBinding().weekDayLabel.setText(dateTime.dayOfWeek().getAsShortText());
    }

    private void invalidateTextColor(DateTime dateTime) {
        boolean isToday = dateTime.withTimeAtStartOfDay().isEqual(DateTime.now().withTimeAtStartOfDay());
        int textColor = ContextCompat.getColor(getContext(), isToday ? R.color.green : R.color.gray_dark);
        getBinding().dateLabel.setTextColor(textColor);
        getBinding().weekDayLabel.setTextColor(textColor);
    }
}
