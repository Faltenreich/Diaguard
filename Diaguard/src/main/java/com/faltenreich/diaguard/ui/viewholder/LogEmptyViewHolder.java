package com.faltenreich.diaguard.ui.viewholder;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.ListItem;
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
    protected TextView textView;

    public LogEmptyViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), EntryActivity.class);
        intent.putExtra(EntryActivity.EXTRA_DATE, getListItem().getDateTime());
        getContext().startActivity(intent);
    }
}
