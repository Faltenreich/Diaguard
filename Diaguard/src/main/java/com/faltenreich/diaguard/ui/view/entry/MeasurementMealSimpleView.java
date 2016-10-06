package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

public class MeasurementMealSimpleView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.meal_value) EditText value;

    public MeasurementMealSimpleView(Context context) {
        super(context, Measurement.Category.MEAL);
    }

    public MeasurementMealSimpleView(Context context, Meal measurement) {
        super(context, measurement);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_meal_simple;
    }

    @Override
    protected void initLayout() {
        value.setText(measurement != null ? Helper.parseFloat(measurement.getCarbohydrates()) : null);
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
        return null;
    }
}
