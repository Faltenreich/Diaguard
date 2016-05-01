package com.faltenreich.diaguard.ui.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemEmpty;
import com.faltenreich.diaguard.ui.activity.EntryActivity;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogEmptyViewHolder extends BaseViewHolder<ListItemEmpty> implements View.OnClickListener {

    @BindView(R.id.empty) TextView textView;

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
