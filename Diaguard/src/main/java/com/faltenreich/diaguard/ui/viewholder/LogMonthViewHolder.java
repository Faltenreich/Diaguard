package com.faltenreich.diaguard.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.LogListSection;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogMonthViewHolder extends BaseViewHolder<LogListSection> {

    @Bind(R.id.background)
    public ImageView background;

    @Bind(R.id.month)
    public TextView month;

    public LogMonthViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData(final LogListSection logListSection) {
        int resourceId = PreferenceHelper.getInstance().getSeasonResourceId(logListSection.getDateTime());
        Picasso.with(getContext()).load(resourceId).placeholder(R.color.gray_lighter).into(background);
        month.setText(logListSection.getDateTime().toString("MMMM YYYY"));
    }
}
