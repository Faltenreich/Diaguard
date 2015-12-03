package com.faltenreich.diaguard.ui.viewholder;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.ListItemDay;
import com.faltenreich.diaguard.adapter.ListItemEmpty;
import com.faltenreich.diaguard.ui.activity.EntryActivity;

import org.joda.time.DateTime;

import butterknife.Bind;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogEmptyViewHolder extends BaseViewHolder<ListItemEmpty> implements View.OnClickListener {

    @Bind(R.id.empty)
    public TextView textView;

    private ListItemEmpty listItem;

    public LogEmptyViewHolder(View view) {
        super(view);
        textView.setOnClickListener(this);
    }

    @Override
    public void bindData(ListItemEmpty listItem) {
        this.listItem = listItem;
    }

    @Override
    public void onClick(View v) {
        if (listItem != null) {
            Intent intent = new Intent(getContext(), EntryActivity.class);
            intent.putExtra(EntryActivity.EXTRA_DATE, listItem.getDay());
            getContext().startActivity(intent);
        }
    }
}
