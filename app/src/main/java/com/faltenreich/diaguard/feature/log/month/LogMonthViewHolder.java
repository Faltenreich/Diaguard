package com.faltenreich.diaguard.feature.log.month;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogMonthViewHolder extends BaseViewHolder<LogMonthListItem> {

    @BindView(R.id.background) ImageView background;
    @BindView(R.id.month) TextView month;

    public LogMonthViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        DateTime dateTime = getListItem().getDateTime();
        month.setText(dateTime.toString("MMMM YYYY"));
        int resourceId = PreferenceHelper.getInstance().getMonthResourceId(dateTime);
        int smallResourceId = PreferenceHelper.getInstance().getMonthSmallResourceId(dateTime);
        ImageLoader.getInstance().load(resourceId, smallResourceId, background, Bitmap.Config.RGB_565);
    }
}
