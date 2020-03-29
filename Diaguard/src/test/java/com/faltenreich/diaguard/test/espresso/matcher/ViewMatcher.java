package com.faltenreich.diaguard.test.espresso.matcher;

import android.view.View;

import androidx.test.espresso.util.TreeIterables;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ViewMatcher {

    public static Matcher<View> withViewCount(final Matcher<View> viewMatcher, final int expectedCount) {
        return new TypeSafeMatcher<View>() {
            int actualCount = -1;

            @Override
            public void describeTo(Description description) {
                if (actualCount >= 0) {
                    description.appendText("With expected number of items: " + expectedCount);
                    description.appendText("\n With matcher: ");
                    viewMatcher.describeTo(description);
                    description.appendText("\n But got: " + actualCount);
                }
            }

            @Override
            protected boolean matchesSafely(View root) {
                actualCount = 0;
                Iterable<View> iterable = TreeIterables.breadthFirstViewTraversal(root);
                actualCount = Iterables.size(Iterables.filter(iterable, withMatcherPredicate(viewMatcher)));
                return actualCount == expectedCount;
            }
        };
    }

    private static Predicate<View> withMatcherPredicate(final Matcher<View> matcher) {
        return matcher::matches;
    }
}
