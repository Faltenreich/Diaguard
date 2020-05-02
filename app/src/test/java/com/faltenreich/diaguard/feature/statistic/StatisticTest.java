package com.faltenreich.diaguard.feature.statistic;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class StatisticTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final CleanUpData cleanUpData = new CleanUpData();

    @Before
    public void setup() {
        FragmentScenario.launchInContainer(StatisticFragment.class);
    }

    private void selectCategory(Category category) {
        Espresso.onView(ViewMatchers.withId(R.id.statistics_categories))
            .perform(ViewActions.click());

        String label = ApplicationProvider.getApplicationContext().getString(category.getStringResId());
        Espresso.onView(ViewMatchers.withText(label))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());
    }

    @Test
    public void onStart_showsBloodSugar() {
        Espresso.onView(ViewMatchers.withText(R.string.bloodsugar))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void onCategorySelected_showsCorrectContent() {
        for (Category category : Category.values()) {
            selectCategory(category);

            String label = ApplicationProvider.getApplicationContext().getString(category.getStringResId());
            Espresso.onView(ViewMatchers.withId(R.id.statistics_categories))
                .check(ViewAssertions.matches(ViewMatchers.withSpinnerText(label)));

            Espresso.onView(ViewMatchers.withText(R.string.average))
                .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

            Espresso.onView(ViewMatchers.withText(R.string.trend))
                .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

            if (category == Category.BLOODSUGAR) {
                Espresso.onView(ViewMatchers.withText(R.string.distribution))
                    .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            }
        }
    }
}
