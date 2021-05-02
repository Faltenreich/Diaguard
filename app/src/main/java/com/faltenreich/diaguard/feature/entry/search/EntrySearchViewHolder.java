package com.faltenreich.diaguard.feature.entry.search;

import android.view.ViewGroup;

import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.feature.log.entry.LogEntryViewHolder;
import com.faltenreich.diaguard.shared.Helper;

import org.joda.time.DateTime;

class EntrySearchViewHolder extends LogEntryViewHolder {

    EntrySearchViewHolder(ViewGroup parent, LogEntryViewHolder.Listener listener) {
        super(parent, listener);
    }

    @Override
    public void onBind(LogEntryListItem item) {
        super.onBind(item);

        ViewGroup container = getBinding().container;
        container.setPadding(container.getPaddingRight(), container.getPaddingTop(), container.getPaddingRight(), container.getPaddingBottom());

        DateTime dateTime = item.getDateTime();
        getBinding().dateLabel.setText(String.format("%s, %s %s",
                dateTime.dayOfWeek().getAsShortText(),
                Helper.getDateFormat().print(dateTime),
                Helper.getTimeFormat().print(dateTime)));
    }
}