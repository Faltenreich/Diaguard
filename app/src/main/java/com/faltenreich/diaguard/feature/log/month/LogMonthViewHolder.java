package com.faltenreich.diaguard.feature.log.month;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemLogMonthBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogMonthViewHolder extends BaseViewHolder<ListItemLogMonthBinding, LogMonthListItem> {

    private static final String MONTH_FORMAT = "MMMM YYYY";

    public LogMonthViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_log_month);
    }

    @Override
    protected ListItemLogMonthBinding createBinding(View view) {
        return ListItemLogMonthBinding.bind(view);
    }

    @Override
    public void onBind(LogMonthListItem item) {
        DateTime dateTime = item.getDateTime();
        invalidateText(dateTime);
        invalidateImage(dateTime);
    }

    private void invalidateText(DateTime dateTime) {
        getBinding().monthLabel.setText(dateTime.toString(MONTH_FORMAT));
    }

    private void invalidateImage(DateTime dateTime) {
        int resourceId = PreferenceStore.getInstance().getMonthResourceId(dateTime);
        int smallResourceId = PreferenceStore.getInstance().getMonthSmallResourceId(dateTime);
        ImageLoader.getInstance().load(resourceId, smallResourceId, getBinding().imageView, Bitmap.Config.RGB_565);
    }
}
