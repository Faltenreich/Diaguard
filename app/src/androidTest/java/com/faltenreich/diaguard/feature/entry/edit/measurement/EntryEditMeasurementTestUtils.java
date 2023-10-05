package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.os.Bundle;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Category;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;

public class EntryEditMeasurementTestUtils {

    public static Bundle createBundle(Category category) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EntryEditFragment.EXTRA_CATEGORY, category);
        return bundle;
    }

    public static void addCategory(Category category) {
        openPickerForCategories();
        selectCategoryFromPicker(category);
    }

    public static void openPickerForCategories() {
        Espresso.onView(AllOf.allOf(
            ViewMatchers.withId(R.id.fab_secondary),
            ViewMatchers.isDisplayed())
        ).perform(ViewActions.click());
    }

    public static void selectCategoryFromPicker(Category category) {
        Espresso.onData(Matchers.anything())
            .inRoot(RootMatchers.isDialog())
            .inAdapterView(ViewMatchers.isAssignableFrom(ListView.class))
            .atPosition(category.ordinal())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK"))
            .perform(ViewActions.click());
    }
}
