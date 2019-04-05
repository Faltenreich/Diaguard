package com.faltenreich.diaguard.ui.list.viewholder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.item.ListItemMonth;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.image.ImageLoader;

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
        month.setText(dateTime.toString("MMMM YYYY"));
        int resourceId = PreferenceHelper.getInstance().getMonthResourceId(dateTime);
        int smallResourceId = PreferenceHelper.getInstance().getMonthSmallResourceId(dateTime);
        ImageLoader.getInstance().load(resourceId, smallResourceId, background, Bitmap.Config.RGB_565);
    }
}
