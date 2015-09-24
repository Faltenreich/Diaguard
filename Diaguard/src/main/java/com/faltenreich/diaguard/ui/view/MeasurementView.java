package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;

import java.lang.reflect.Constructor;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class MeasurementView<T extends Measurement> extends LinearLayout {

    private static final String TAG = MeasurementView.class.getSimpleName();

    @Bind(R.id.image_showcase)
    protected ImageView imageViewShowcase;

    @Bind(R.id.layer_showcase)
    protected View viewLayerShowcase;

    @Bind(R.id.image_category)
    protected ImageView imageViewCategory;

    @Bind(R.id.category)
    protected TextView textViewCategory;

    @Bind(R.id.button_delete)
    protected ImageView buttonDelete;

    @Bind(R.id.layout_content)
    protected LinearLayout content;

    private T measurement;

    @Deprecated
    public MeasurementView(Context context) {
        super(context);
    }

    public MeasurementView(Context context, T measurement) {
        super(context);
        this.measurement = measurement;
        init();
    }

    @SuppressWarnings("unchecked")
    public MeasurementView(Context context, Measurement.Category category) {
        super(context);
        try {
            Constructor<T> constructor = category.toClass().getConstructor();
            measurement = constructor.newInstance();
        } catch (Exception exception) {
            Log.e(TAG, String.format("Could not instantiate object of class '%s'", category.toClass().getSimpleName()));
        }
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement, this);
        ButterKnife.bind(this);

        if (measurement instanceof Insulin) {

        } else if (measurement instanceof Meal) {

        } else if (measurement instanceof Pressure) {

        } else {
            content.addView(new MeasurementGenericView(getContext(), measurement));
        }
    }

    public Measurement getMeasurement() {
        try {
            return ((MeasurementAbstractView) getChildAt(0)).getMeasurement();
        } catch (Exception exception) {
            Log.e(TAG, "Could not return measurement");
            return null;
        }
    }

}
