package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemMeasurementBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.swipe.SwipeDismissTouchListener;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class MeasurementView<T extends Measurement> extends CardView implements ViewBindable<ListItemMeasurementBinding> {

    private Category category;
    @Nullable private T measurement;
    @Nullable private Food food;

    private OnCategoryRemovedListener onCategoryRemovedListener;

    private ListItemMeasurementBinding binding;

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
        this.category = Category.MEAL;
        this.food = food;
        init();
    }

    public MeasurementView(Context context, @NonNull Category category) {
        super(context);
        this.category = category;
        init();
    }

    @Override
    public ListItemMeasurementBinding getBinding() {
        return binding;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement, this);
        binding = ListItemMeasurementBinding.bind(this);
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

        setOnTouchListener(new SwipeDismissTouchListener(this, null, new SwipeDismissTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(Object token) {
                return true;
            }

            @Override
            public void onDismiss(View view, Object token) {
                remove();
            }
        }));
        getBinding().deleteButton.setOnClickListener((view) -> remove());

        CheckBox pinnedCheckbox = getBinding().pinnedCheckbox;
        pinnedCheckbox.setChecked(PreferenceStore.getInstance().isCategoryPinned(category));
        pinnedCheckbox.setOnCheckedChangeListener((checkbox, isChecked) -> togglePinnedCategory(isChecked));
    }

    private void initData() {
        String categoryName = getContext().getString(category.getStringResId());
        getBinding().showcaseImageView.setImageResource(category.getShowcaseImageResourceId());
        getBinding().categoryImageView.setImageResource(category.getIconImageResourceId());
        getBinding().categoryLabel.setText(categoryName);
        getBinding().deleteButton.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), categoryName));

        boolean isUpdating = measurement != null;
        switch (category) {
            case INSULIN:
                getBinding().contentLayout.addView(isUpdating ?
                    new MeasurementInsulinView(getContext(), (Insulin) measurement) :
                    new MeasurementInsulinView(getContext()));
                break;
            case MEAL:
                getBinding().contentLayout.addView(isUpdating ?
                    new MeasurementMealView(getContext(), (Meal) measurement) :
                    new MeasurementMealView(getContext(), food));
                break;
            case PRESSURE:
                getBinding().contentLayout.addView(isUpdating ?
                    new MeasurementPressureView(getContext(), (Pressure) measurement) :
                    new MeasurementPressureView(getContext()));
                break;
            default:
                getBinding().contentLayout.addView(isUpdating ?
                    new MeasurementGenericView<>(getContext(), measurement) :
                    new MeasurementGenericView<>(getContext(), category));
        }
    }

    public Measurement getMeasurement() {
        View childView = getBinding().contentLayout.getChildAt(0);
        if (childView instanceof MeasurementAbstractView) {
            return ((MeasurementAbstractView<?, ?>) childView).getMeasurement();
        } else {
            return null;
        }
    }

    public void setOnCategoryRemovedListener(OnCategoryRemovedListener onCategoryRemovedListener) {
        this.onCategoryRemovedListener = onCategoryRemovedListener;
    }

    private void togglePinnedCategory(final boolean isPinned) {
        CheckBox pinnedCheckbox = getBinding().pinnedCheckbox;
        int textResId = isPinned ? R.string.category_pin_confirm : R.string.category_unpin_confirm;
        String categoryString = getContext().getString(category.getStringResId());
        String confirmation = String.format(getContext().getString(textResId), categoryString);
        ViewUtils.showSnackbar(MeasurementView.this, confirmation, view -> {
            pinnedCheckbox.setOnCheckedChangeListener(null);
            pinnedCheckbox.setChecked(!isPinned);
            PreferenceStore.getInstance().setCategoryPinned(category, !isPinned);
            pinnedCheckbox.setOnCheckedChangeListener((checkbox, isChecked) -> togglePinnedCategory(isChecked));
        });
        PreferenceStore.getInstance().setCategoryPinned(category, isPinned);
    }

    private void remove() {
        if (onCategoryRemovedListener != null) {
            onCategoryRemovedListener.onRemove(category);
        }
    }

    public interface OnCategoryRemovedListener {
        void onRemove(Category category);
    }
}
