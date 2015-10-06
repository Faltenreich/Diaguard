package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
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

import java.util.HashMap;

/**
 * Created by Faltenreich on 30.09.2015.
 */
public class MeasurementFloatingActionMenu extends FloatingActionMenu {

    private static final int MAX_BUTTON_COUNT = 3;

    private MeasurementFloatingActionMenuCallback measurementFloatingActionMenuCallback;
    private HashMap<Measurement.Category, FloatingActionButton> menuButtons;

    public MeasurementFloatingActionMenu(Context context) {
        super(context);
    }

    public MeasurementFloatingActionMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void init() {
        menuButtons = new HashMap<>();

        // Close FAB on click outside
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

        FloatingActionButton fabAll = getFloatingActionButton(
                getContext().getString(R.string.all),
                R.drawable.ic_other,
                android.R.color.white);
        fabAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close(true);
                if (hasMeasurementFloatingActionMenuCallback()) {
                    measurementFloatingActionMenuCallback.onMiscellaneousSelected();
                }
            }
        });
        addMenuButton(fabAll);

        restock();
    }

    /**
     * FAB count must always be equals MAX_BUTTON_COUNT, so we restock in case this count changes
     */
    private void restock() {
        Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        int position = 0;
        while (position < activeCategories.length && menuButtons.size() < MAX_BUTTON_COUNT) {
            Measurement.Category category = activeCategories[position];
            if (!hasCategory(category)) {
                addCategory(category);
            }
            position++;
        }
    }

    public boolean hasCategory(Measurement.Category category) {
        return menuButtons.get(category) != null;
    }

    public void addCategory(final Measurement.Category category) {
        FloatingActionButton fab = getFloatingActionButton(
                category.toString(),
                PreferenceHelper.getInstance().getCategoryImageResourceId(category),
                PreferenceHelper.getInstance().getCategoryColorResourceId(category));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(true);
                if (hasMeasurementFloatingActionMenuCallback()) {
                    measurementFloatingActionMenuCallback.onCategorySelected(category);
                    //removeCategory(category);
                }
            }
        });

        addMenuButton(fab, menuButtons.size());
        menuButtons.put(category, fab);
    }

    public void removeCategory(Measurement.Category category) {
        if (hasCategory(category)) {
            removeMenuButton(menuButtons.get(category));
            menuButtons.remove(category);
            restock();
        }
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

    public void setMeasurementFloatingActionMenuCallback(MeasurementFloatingActionMenuCallback measurementFloatingActionMenuCallback) {
        this.measurementFloatingActionMenuCallback = measurementFloatingActionMenuCallback;
    }

    private boolean hasMeasurementFloatingActionMenuCallback() {
        return measurementFloatingActionMenuCallback != null;
    }

    public interface MeasurementFloatingActionMenuCallback {
        void onCategorySelected(Measurement.Category category);
        void onMiscellaneousSelected();
    }

}
