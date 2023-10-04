package com.faltenreich.diaguard.test.espresso;

import androidx.annotation.IntegerRes;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.CoreMatchers;

public class SnackbarUtils {

   public static void assertDisplayedSnackbar(@IntegerRes int textRes) {
      assertDisplayedSnackbarText(textRes);
   }

   public static void assertDisplayedSnackbar(String text) {
      assertDisplayedSnackbarText(text);
   }

   private static void assertDisplayedSnackbarText(@IntegerRes int textRes) {
      Espresso.onView(
          CoreMatchers.allOf(
              ViewMatchers.withId(com.google.android.material.R.id.snackbar_action),
              ViewMatchers.withText(textRes)
          )
      );
   }

   private static void assertDisplayedSnackbarText(String text) {
      Espresso.onView(
          CoreMatchers.allOf(
              ViewMatchers.withId(com.google.android.material.R.id.snackbar_action),
              ViewMatchers.withText(text)
          )
      );
   }

   private static void assertDisplayedSnackbarView(@IntegerRes int textRes) {
      Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
          .check(ViewAssertions.matches(ViewMatchers.withText(textRes)));
   }
}
