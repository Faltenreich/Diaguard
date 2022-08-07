package com.faltenreich.diaguard.test.espresso;

import android.content.Context;

import androidx.annotation.IntegerRes;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.test.robolectric.ShadowSnackbar;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

public class SnackbarUtils {

   public static void assertDisplayedSnackbar(@IntegerRes int textRes) {
      assertDisplayedSnackbarText(textRes);
   }

   public static void assertDisplayedSnackbar(String text) {
      assertDisplayedSnackbarText(text);
   }

   private static void assertDisplayedSnackbarText(@IntegerRes int textRes) {
      Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
      MatcherAssert.assertThat(ShadowSnackbar.getTextOfLatestSnackbar(), CoreMatchers.is(context.getString(textRes)));
   }

   private static void assertDisplayedSnackbarText(String text) {
      MatcherAssert.assertThat(ShadowSnackbar.getTextOfLatestSnackbar(), CoreMatchers.is(text));
   }

   private static void assertDisplayedSnackbarView(@IntegerRes int textRes) {
      Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
          .check(ViewAssertions.matches(ViewMatchers.withText(textRes)));
   }
}
