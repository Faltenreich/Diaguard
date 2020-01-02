package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.ui.list.helper.SwipeDismissTouchListener;
import com.faltenreich.diaguard.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class MeasurementView<T extends Measurement> extends CardView implements CompoundButton.OnCheckedChangeListener, SwipeDismissTouchListener.DismissCallbacks {

    @BindView(R.id.image_showcase) ImageView imageViewShowcase;
    @BindView(R.id.image_category) ImageView imageViewCategory;
    @BindView(R.id.category) TextView textViewCategory;
    @BindView(R.id.layout_content) ViewGroup content;
    @BindView(R.id.checkbox_pin) CheckBox checkBoxPin;
    @BindView(R.id.button_delete) View buttonRemove;

    private Measurement.Category category;
    @Nullable private T measurement;
    @Nullable private Food food;

    private OnCategoryRemovedListener onCategoryRemovedListener;

    @Deprecated
    public MeasurementView(Context context) {
        super(context);
    }

    public MeasurementView(Context context, @NonNull T measurement) {
        super(context);
        this.category = measurement.getCategory();
        this.measurement = measurement;
        init();
    }

    public MeasurementView(Context context, @NonNull Food food) {
        super(context);
        this.category = Measurement.Category.MEAL;
        this.food = food;
        init();
    }

    public MeasurementView(Context context, @NonNull Measurement.Category category) {
        super(context);
        this.category = category;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement, this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initLayout();
        initData();
    }

    private void initLayout() {
        setRadius(getContext().getResources().getDimension(R.dimen.card_corner_radius));
        setCardElevation(getContext().getResources().getDimension(R.dimen.card_elevation));
        setUseCompatPadding(true);

        setOnTouchListener(new SwipeDismissTouchListener(this, null, this));
        checkBoxPin.setChecked(PreferenceHelper.getInstance().isCategoryPinned(category));
        checkBoxPin.setOnCheckedChangeListener(this);
    }

    private void initData() {
        String categoryName = PreferenceHelper.getInstance().getCategoryName(category);
        imageViewShowcase.setImageResource(PreferenceHelper.getInstance().getShowcaseImageResourceId(category));
        imageViewCategory.setImageResource(PreferenceHelper.getInstance().getCategoryImageResourceId(category));
        textViewCategory.setText(categoryName);
        buttonRemove.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), categoryName));

        boolean isUpdating = measurement != null;
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
        if (childView instanceof MeasurementAbstractView) {
            return ((MeasurementAbstractView) childView).getMeasurement();
        } else {
            return null;
        }
    }

    public void setOnCategoryRemovedListener(OnCategoryRemovedListener onCategoryRemovedListener) {
        this.onCategoryRemovedListener = onCategoryRemovedListener;
    }

    private void togglePinnedCategory(final boolean isPinned) {
        int textResId = isPinned ? R.string.category_pin_confirm : R.string.category_unpin_confirm;
        String categoryString = getContext().getString(category.getStringResId());
        String confirmation = String.format(getContext().getString(textResId), categoryString);
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
        if (onCategoryRemovedListener != null) {
            onCategoryRemovedListener.onRemove(category);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        togglePinnedCategory(isChecked);
    }

    @Override
    public boolean canDismiss(Object token) {
        return true;
    }

    @Override
    public void onDismiss(View view, Object token) {
        remove();
    }

    public interface OnCategoryRemovedListener {
        void onRemove(Measurement.Category category);
    }
}
