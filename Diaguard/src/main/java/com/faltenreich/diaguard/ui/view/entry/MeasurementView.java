package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.helper.SwipeDismissTouchListener;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class MeasurementView<T extends Measurement> extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.image_showcase) ImageView imageViewShowcase;
    @BindView(R.id.layer_showcase) View viewLayerShowcase;
    @BindView(R.id.image_category) ImageView imageViewCategory;
    @BindView(R.id.category) TextView textViewCategory;
    @BindView(R.id.layout_content) ViewGroup content;
    @BindView(R.id.checkbox_pin) CheckBox checkBoxPin;
    @BindView(R.id.button_delete) View buttonRemove;

    private Measurement.Category category;
    private OnCategoryRemovedListener onCategoryRemovedListener;

    @Deprecated
    public MeasurementView(Context context) {
        super(context);
    }

    public MeasurementView(Context context, T measurement) {
        super(context);
        this.category = measurement.getCategory();
        init(measurement, null);
    }

    public MeasurementView(Context context, Food food) {
        super(context);
        this.category = Measurement.Category.MEAL;
        init(null, food);
    }

    public MeasurementView(Context context, Measurement.Category category) {
        super(context);
        this.category = category;
        init(null, null);
    }

    public void setOnCategoryRemovedListener(OnCategoryRemovedListener onCategoryRemovedListener) {
        this.onCategoryRemovedListener = onCategoryRemovedListener;
    }

    private boolean hasMeasurementViewCallback() {
        return onCategoryRemovedListener != null;
    }

    private void init(@Nullable Measurement measurement, @Nullable Food food) {
        boolean isUpdating = measurement != null;

        LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement, this);
        ButterKnife.bind(this);

        String categoryName = PreferenceHelper.getInstance().getCategoryName(category);
        imageViewShowcase.setImageResource(PreferenceHelper.getInstance().getShowcaseImageResourceId(category));
        imageViewCategory.setImageResource(PreferenceHelper.getInstance().getCategoryImageResourceId(category));
        textViewCategory.setText(categoryName);
        checkBoxPin.setChecked(PreferenceHelper.getInstance().isCategoryPinned(category));
        checkBoxPin.setOnCheckedChangeListener(this);
        buttonRemove.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), categoryName));

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
            case MEAL:
                content.addView(isUpdating ?
                        new MeasurementMealView(getContext(), (Meal) measurement) :
                        new MeasurementMealView(getContext(), food));
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

    private void togglePinnedCategory(final boolean isPinned) {
        int textResId = isPinned ? R.string.category_pin_confirm : R.string.category_unpin_confirm;
        String confirmation = String.format(getContext().getString(textResId), category.toLocalizedString(getContext()));
        ViewUtils.showSnackbar(MeasurementView.this, confirmation, view -> {
            checkBoxPin.setOnCheckedChangeListener(null);
            checkBoxPin.setChecked(!isPinned);
            PreferenceHelper.getInstance().setCategoryPinned(category, !isPinned);
            checkBoxPin.setOnCheckedChangeListener(MeasurementView.this);
        });
        PreferenceHelper.getInstance().setCategoryPinned(category, isPinned);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_delete)
    protected void remove() {
        if (hasMeasurementViewCallback()) {
            onCategoryRemovedListener.onRemove(category);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        togglePinnedCategory(isChecked);
    }

    public interface OnCategoryRemovedListener {
        void onRemove(Measurement.Category category);
    }
}
