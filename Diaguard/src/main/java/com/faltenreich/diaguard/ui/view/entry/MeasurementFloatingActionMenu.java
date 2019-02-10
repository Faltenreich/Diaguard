package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.FloatingActionButtonFactory;
import com.faltenreich.diaguard.util.ResourceUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * Created by Faltenreich on 30.09.2015.
 */
// TODO: Change icon color according to theme
public class MeasurementFloatingActionMenu extends FloatingActionMenu {

    private static final int MAX_BUTTON_COUNT = 3;

    private List<Measurement.Category> categoriesToSkip;
    private OnFabSelectedListener onFabSelectedListener;

    public MeasurementFloatingActionMenu(Context context) {
        super(context);
        init();
    }

    public MeasurementFloatingActionMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        categoriesToSkip = new ArrayList<>();
        enableCloseOnClickOutside();
    }

    private void enableCloseOnClickOutside() {
        setOnTouchListener((view, event) -> {
            if (isOpened()) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    close(true);
                }
                return true;
            } else {
                return false;
            }
        });
    }

    public void ignore(Measurement.Category category) {
        if (!categoriesToSkip.contains(category)) {
            categoriesToSkip.add(category);
        }
    }

    public void removeIgnore(Measurement.Category category) {
        if (categoriesToSkip.contains(category)) {
            categoriesToSkip.remove(category);
        }
    }

    private boolean hasChanged() {
        // TODO: Check whether ignores have changed
        return true;
    }

    public void restock() {
        if (hasChanged()) {
            removeAllMenuButtons();

            Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
            int menuButtonCount = 0;
            int position = 0;
            while (position < activeCategories.length && menuButtonCount < MAX_BUTTON_COUNT) {
                Measurement.Category category = activeCategories[position];
                if (!categoriesToSkip.contains(category)) {
                    addMenuButton(category);
                    menuButtonCount++;
                }
                position++;
            }

            FloatingActionButton fabAll = FloatingActionButtonFactory.createFloatingActionButton(
                    getContext(),
                    getContext().getString(R.string.all),
                    R.drawable.ic_more,
                    ResourceUtils.getBackgroundSecondary(getContext()));
            
            addMenuButton(fabAll);

            fabAll.setOnClickListener(v -> {
                close(true);
                if (hasMeasurementFloatingActionMenuCallback()) {
                    onFabSelectedListener.onMiscellaneousSelected();
                }
            });
        }
    }

    public void addMenuButton(final Measurement.Category category) {
        FloatingActionButton fab = FloatingActionButtonFactory.createFloatingActionButton(
                getContext(),
                category.toLocalizedString(),
                PreferenceHelper.getInstance().getCategoryImageResourceId(category),
                ContextCompat.getColor(getContext(), R.color.green));
        fab.setOnClickListener(v -> {
            close(true);
            if (hasMeasurementFloatingActionMenuCallback()) {
                onFabSelectedListener.onCategorySelected(category);
            }
        });
        addMenuButton(fab);
    }

    public void setOnFabSelectedListener(OnFabSelectedListener onFabSelectedListener) {
        this.onFabSelectedListener = onFabSelectedListener;
    }

    private boolean hasMeasurementFloatingActionMenuCallback() {
        return onFabSelectedListener != null;
    }

    public interface OnFabSelectedListener {
        void onCategorySelected(Measurement.Category category);
        void onMiscellaneousSelected();
    }
}
