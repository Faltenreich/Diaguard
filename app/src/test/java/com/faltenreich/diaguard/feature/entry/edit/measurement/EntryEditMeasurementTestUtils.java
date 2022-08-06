package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.os.Bundle;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Category;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.AllOf;

public class EntryEditMeasurementTestUtils {

    public static Bundle createBundle(Category category) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EntryEditFragment.EXTRA_CATEGORY, category);
        return bundle;
    }

    public static void addCategory(Category category) {
        openFloatingMenuForCategories();
        openPickerForCategories();
        selectCategoryFromPicker(category);
    }

    public static void openFloatingMenuForCategories() {
        Espresso.onView(AllOf.allOf(
            ViewMatchers.withParent(ViewMatchers.withId(R.id.fab_menu)),
            ViewMatchers.withClassName(CoreMatchers.endsWith("ImageView")),
            ViewMatchers.isDisplayed())
        ).perform(ViewActions.click());
    }

    public static void openPickerForCategories() {
        Espresso.onView(ViewMatchers.withText("All"))
            .perform(ViewActions.click());
    }

    public static void selectCategoryFromPicker(Category category) {
        Espresso.onView(ViewMatchers.withText(category.getStringResId()))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.scrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK"))
            .perform(ViewActions.click());
    }
}
