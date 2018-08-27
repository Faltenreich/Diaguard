package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 30.09.2015.
 */
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
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isOpened()) {
                    if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        close(true);
                    }
                    return true;
                } else {
                    return false;
                }
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

            FloatingActionButton fabAll = getFloatingActionButton(
                    getContext().getString(R.string.all),
                    R.drawable.ic_other,
                    android.R.color.white);
            
            addMenuButton(fabAll);

            fabAll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    close(true);
                    if (hasMeasurementFloatingActionMenuCallback()) {
                        onFabSelectedListener.onMiscellaneousSelected();
                    }
                }
            });
        }
    }

    public void addMenuButton(final Measurement.Category category) {
        FloatingActionButton fab = getFloatingActionButton(
                category.toLocalizedString(),
                PreferenceHelper.getInstance().getCategoryImageResourceId(category),
                R.color.green);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(true);
                if (hasMeasurementFloatingActionMenuCallback()) {
                    onFabSelectedListener.onCategorySelected(category);
                }
            }
        });
        addMenuButton(fab);
    }

    private FloatingActionButton getFloatingActionButton(String text, int imageResourceId, int colorResId) {
        FloatingActionButton floatingActionButton = new FloatingActionButton(getContext());
        floatingActionButton.setButtonSize(FloatingActionButton.SIZE_MINI);
        floatingActionButton.setLabelText(text);
        floatingActionButton.setImageResource(imageResourceId);
        floatingActionButton.setColorNormalResId(colorResId);
        float brighteningPercentage = colorResId == android.R.color.white ? .9f : 1.2f;
        int colorHighlight = Helper.colorBrighten(ContextCompat.getColor(getContext(), colorResId), brighteningPercentage);
        floatingActionButton.setColorPressed(colorHighlight);
        floatingActionButton.setColorRipple(colorHighlight);
        return floatingActionButton;
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
