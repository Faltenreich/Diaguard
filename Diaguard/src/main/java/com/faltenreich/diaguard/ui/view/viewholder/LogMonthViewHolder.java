package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemMonth;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogMonthViewHolder extends BaseViewHolder<ListItemMonth> {

    @BindView(R.id.background) ImageView background;
    @BindView(R.id.month) TextView month;

    public LogMonthViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        DateTime dateTime = getListItem().getDateTime();
        int resourceId = PreferenceHelper.getInstance().getSeasonResourceId(dateTime);
        Picasso.with(getContext()).load(resourceId).placeholder(R.color.gray_lighter).into(background);
        month.setText(dateTime.toString("MMMM YYYY"));
    }
}
