package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;

import com.faltenreich.diaguard.ui.list.adapter.EntrySearchAdapter;
import com.faltenreich.diaguard.util.Helper;

import org.joda.time.DateTime;

public class EntrySearchViewHolder extends LogEntryViewHolder {

    public EntrySearchViewHolder(View view, EntrySearchAdapter.OnSearchItemClickListener listener) {
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
