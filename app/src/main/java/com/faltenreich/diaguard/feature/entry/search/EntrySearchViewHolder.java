package com.faltenreich.diaguard.feature.entry.search;

import android.view.ViewGroup;

import com.faltenreich.diaguard.feature.log.entry.LogEntryViewHolder;
import com.faltenreich.diaguard.shared.Helper;

import org.joda.time.DateTime;

class EntrySearchViewHolder extends LogEntryViewHolder {

    EntrySearchViewHolder(ViewGroup parent, EntrySearchListAdapter.OnSearchItemClickListener listener) {
        super(parent, listener);
    }

    @Override
    public void bind() {
        super.bind();

        rootLayout.setPadding(rootLayout.getPaddingRight(), rootLayout.getPaddingTop(), rootLayout.getPaddingRight(), rootLayout.getPaddingBottom());

        DateTime dateTime = getItem().getDateTime();
        dateTimeView.setText(String.format("%s, %s %s",
                dateTime.dayOfWeek().getAsShortText(),
                Helper.getDateFormat().print(dateTime),
                Helper.getTimeFormat().print(dateTime)));
    }
}