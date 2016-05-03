package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SwipeDismissTouchListener;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.util.ViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class MeasurementView<T extends Measurement> extends LinearLayout {

    @BindView(R.id.image_showcase) ImageView imageViewShowcase;
    @BindView(R.id.layer_showcase) View viewLayerShowcase;
    @BindView(R.id.image_category) ImageView imageViewCategory;
    @BindView(R.id.category) TextView textViewCategory;
    @BindView(R.id.layout_content) LinearLayout content;
    @BindView(R.id.checkbox_pin) CheckBox checkBoxPin;

    private Measurement.Category category;
    private OnCategoryRemovedListener onCategoryRemovedListener;

    @Deprecated
    public MeasurementView(Context context) {
        super(context);
    }

    public MeasurementView(Context context, T measurement) {
        super(context);
        this.category = measurement.getCategory();
        init(measurement);
    }

    public MeasurementView(Context context, Measurement.Category category) {
        super(context);
        this.category = category;
        init(null);
    }

    public void setOnCategoryRemovedListener(OnCategoryRemovedListener onCategoryRemovedListener) {
        this.onCategoryRemovedListener = onCategoryRemovedListener;
    }

    private boolean hasMeasurementViewCallback() {
        return onCategoryRemovedListener != null;
    }

    private void init(@Nullable Measurement measurement) {
        boolean isUpdating = measurement != null;

        LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement, this);
        ButterKnife.bind(this);

        imageViewShowcase.setImageResource(PreferenceHelper.getInstance().getShowcaseImageResourceId(category));
        viewLayerShowcase.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        imageViewCategory.setImageResource(PreferenceHelper.getInstance().getCategoryImageResourceId(category));
        textViewCategory.setText(PreferenceHelper.getInstance().getCategoryName(category));
        checkBoxPin.setChecked(PreferenceHelper.getInstance().isCategoryPinned(category));
        checkBoxPin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewHelper.showToast(getContext(),
                        getContext().getString(isChecked ?
                                R.string.category_pin_confirm :
                                R.string.category_unpin_confirm),
                        Toast.LENGTH_SHORT);
                PreferenceHelper.getInstance().setCategoryPinned(category, isChecked);
            }
        });

        setOnTouchListener(new SwipeDismissTouchListener(this, null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }
                    @Override
                    public void onDismiss(View view, Object token) {
                        if (hasMeasurementViewCallback()) {
                            onCategoryRemovedListener.onRemove(category);
                        }
                    }
                }));

        switch (category) {
            case INSULIN:
                content.addView(isUpdating ?
                        new MeasurementInsulinView(getContext(), (Insulin) measurement) :
                        new MeasurementInsulinView(getContext()));
                break;
            case PRESSURE:
                content.addView(isUpdating ?
                        new MeasurementPressureView(getContext(), (Pressure) measurement) :
                        new MeasurementPressureView(getContext()));
                break;
            default:
                content.addView(isUpdating ?
                        new MeasurementGenericView<>(getContext(), measurement) :
                        new MeasurementGenericView<>(getContext(), category));
        }
    }

    public Measurement getMeasurement() {
        View childView = content.getChildAt(0);
        if (childView != null && childView instanceof MeasurementAbstractView) {
            return ((MeasurementAbstractView) childView).getMeasurement();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_delete)
    protected void remove() {
        if (hasMeasurementViewCallback()) {
            onCategoryRemovedListener.onRemove(category);
        }
    }

    public interface OnCategoryRemovedListener {
        void onRemove(Measurement.Category category);
    }
}
