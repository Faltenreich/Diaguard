package com.faltenreich.diaguard.feature.log.month;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogMonthViewHolder extends BaseViewHolder<LogMonthListItem> {

    @BindView(R.id.background) ImageView background;
    @BindView(R.id.month) TextView month;

    public LogMonthViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_log_month);
    }

    @Override
    public void onBind(LogMonthListItem item) {
        DateTime dateTime = item.getDateTime();
        month.setText(dateTime.toString("MMMM YYYY"));
        int resourceId = PreferenceStore.getInstance().getMonthResourceId(dateTime);
        int smallResourceId = PreferenceStore.getInstance().getMonthSmallResourceId(dateTime);
        ImageLoader.getInstance().load(resourceId, smallResourceId, background, Bitmap.Config.RGB_565);
    }
}
