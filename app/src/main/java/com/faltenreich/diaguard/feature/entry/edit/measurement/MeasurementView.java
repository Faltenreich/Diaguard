package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemMeasurementBinding;
import com.faltenreich.diaguard.feature.entry.edit.input.GenericInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.InsulinInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.MealInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.MeasurementInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.PressureInputView;
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

    private ImageView deleteButton;
    private CheckBox pinnedCheckbox;
    private ImageView showcaseImageView;
    private ImageView categoryImageView;
    private TextView categoryLabel;
    private ViewGroup contentLayout;

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
        binding = ListItemMeasurementBinding.inflate(LayoutInflater.from(getContext()), this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        bindViews();
        initLayout();
        initData();
    }

    private void bindViews() {
        deleteButton = getBinding().deleteButton;
        pinnedCheckbox = getBinding().pinnedCheckbox;
        showcaseImageView = getBinding().showcaseImageView;
        categoryImageView = getBinding().categoryImageView;
        categoryLabel = getBinding().categoryLabel;
        contentLayout = getBinding().contentLayout;
    }

    private void initLayout() {
        setRadius(getContext().getResources().getDimension(R.dimen.card_corner_radius));
        setCardElevation(getContext().getResources().getDimension(R.dimen.card_elevation));
        setUseCompatPadding(true);
        setOnTouchListener(new SwipeDismissTouchListener(this, null, new SwipeDismissTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(Object token) { return true; }
            @Override
            public void onDismiss(View view, Object token) { remove(); }
        }));

        deleteButton.setOnClickListener((view) -> remove());

        pinnedCheckbox.setChecked(PreferenceStore.getInstance().isCategoryPinned(category));
        pinnedCheckbox.setOnCheckedChangeListener((checkbox, isChecked) -> togglePinnedCategory(isChecked));
    }

    private void initData() {
        String categoryName = getContext().getString(category.getStringResId());
        showcaseImageView.setImageResource(category.getShowcaseImageResourceId());
        categoryImageView.setImageResource(category.getIconImageResourceId());
        categoryLabel.setText(categoryName);
        deleteButton.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), categoryName));

        boolean isUpdating = measurement != null;
        switch (category) {
            case INSULIN:
                contentLayout.addView(isUpdating ?
                    new InsulinInputView(getContext(), (Insulin) measurement) :
                    new InsulinInputView(getContext()));
                break;
            case MEAL:
                contentLayout.addView(isUpdating ?
                    new MealInputView(getContext(), (Meal) measurement) :
                    new MealInputView(getContext(), food));
                break;
            case PRESSURE:
                contentLayout.addView(isUpdating ?
                    new PressureInputView(getContext(), (Pressure) measurement) :
                    new PressureInputView(getContext()));
                break;
            default:
                contentLayout.addView(isUpdating ?
                    new GenericInputView<>(getContext(), measurement) :
                    new GenericInputView<>(getContext(), category));
        }
    }

    public Measurement getMeasurement() {
        View childView = contentLayout.getChildAt(0);
        if (childView instanceof MeasurementInputView) {
            return ((MeasurementInputView<?, ?>) childView).getMeasurement();
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
