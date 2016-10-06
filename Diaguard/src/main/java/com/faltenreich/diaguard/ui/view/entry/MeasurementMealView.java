package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.MeasurementMealPagerAdapter;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;


    public MeasurementMealView(Context context) {
        super(context, Measurement.Category.MEAL);
    }

    public MeasurementMealView(Context context, Meal meal) {
        super(context, meal);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_meal;
    }

    @Override
    protected void initLayout() {
        viewPager.setAdapter(new MeasurementMealPagerAdapter(getContext()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void setValues() {

    }

    @Override
    protected boolean isValid() {
        return false;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            return measurement;
        } else {
            return null;
        }
    }
}