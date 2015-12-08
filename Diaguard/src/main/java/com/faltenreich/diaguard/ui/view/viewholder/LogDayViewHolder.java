package com.faltenreich.diaguard.ui.view.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemDay;

import org.joda.time.DateTime;

import butterknife.Bind;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogDayViewHolder extends BaseViewHolder<ListItemDay> {

    @Bind(R.id.day)
    protected TextView day;

    @Bind(R.id.weekday)
    protected TextView weekDay;

    public LogDayViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        DateTime dateTime = getListItem().getDateTime();

        day.setText(dateTime.toString("dd"));
        weekDay.setText(dateTime.dayOfWeek().getAsShortText());

        // Highlight current day
        boolean isToday = dateTime.withTimeAtStartOfDay().isEqual(DateTime.now().withTimeAtStartOfDay());
        int textColor =  isToday ?
                ContextCompat.getColor(getContext(), R.color.green) :
                ContextCompat.getColor(getContext(), R.color.gray_dark);
        day.setTextColor(textColor);
        weekDay.setTextColor(textColor);
    }
}
