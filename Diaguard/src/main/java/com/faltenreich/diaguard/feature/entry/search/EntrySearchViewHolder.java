package com.faltenreich.diaguard.feature.entry.search;

import android.view.View;

import com.faltenreich.diaguard.feature.log.entry.LogEntryViewHolder;
import com.faltenreich.diaguard.shared.Helper;

import org.joda.time.DateTime;

class EntrySearchViewHolder extends LogEntryViewHolder {

    EntrySearchViewHolder(View view, EntrySearchListAdapter.OnSearchItemClickListener listener) {
        super(view, listener);
    }

    @Override
    public void bindData() {
        super.bindData();

        rootLayout.setPadding(rootLayout.getPaddingRight(), rootLayout.getPaddingTop(), rootLayout.getPaddingRight(), rootLayout.getPaddingBottom());

        DateTime dateTime = getListItem().getDateTime();
        dateTimeView.setText(String.format("%s, %s %s",
                dateTime.dayOfWeek().getAsShortText(),
                Helper.getDateFormat().print(dateTime),
                Helper.getTimeFormat().print(dateTime)));
    }
}