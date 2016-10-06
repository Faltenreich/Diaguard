package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.view.entry.MeasurementMealExtendedView;
import com.faltenreich.diaguard.ui.view.entry.MeasurementMealSimpleView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

public class MeasurementMealPagerAdapter extends PagerAdapter {

    private enum MeasurementMealPage {

        SIMPLE(R.string.value),
        FOOD(R.string.food);

        public int stringResId;

        MeasurementMealPage(@StringRes int stringResId) {
            this.stringResId = stringResId;
        }

        public View getView(Context context) {
            switch (this) {
                case SIMPLE: return new MeasurementMealSimpleView(context);
                case FOOD: return new MeasurementMealExtendedView(context);
                default: return null;
            }
        }
    }

    private Context context;

    public MeasurementMealPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        MeasurementMealPage page = MeasurementMealPage.values()[position];
        View view = page.getView(context);
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return MeasurementMealPage.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MeasurementMealPage page = MeasurementMealPage.values()[position];
        return context.getString(page.stringResId);
    }
}