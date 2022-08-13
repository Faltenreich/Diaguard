package com.faltenreich.diaguard.test.robolectric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.ContentViewCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/cafesilencio/snackbarshadow/blob/master/ShadowSnackbar.java
 */
@SuppressWarnings({"UnusedDeclaration", "Unchecked"})
@Implements(Snackbar.class)
public class ShadowSnackbar {

   static List<ShadowSnackbar> shadowSnackbars = new ArrayList<>();

   @RealObject Snackbar snackbar;

   String text;
   private int duration;
   private int gravity;
   private int xOffset;
   private int yOffset;
   private View view;

   @Implementation
   public static Snackbar make(@NonNull View view, @NonNull CharSequence text, int duration) {
      Snackbar snackbar = null;
      try {
         Constructor<Snackbar> constructor = Snackbar.class.getDeclaredConstructor(
             Context.class,
             ViewGroup.class,
             View.class,
             ContentViewCallback.class
         );
         if (Modifier.isPrivate(constructor.getModifiers())) {
            constructor.setAccessible(true);
         }

         Context context = view.getContext();
         ViewGroup parent = findSuitableParent(view);
         final SnackbarContentLayout content =
             (SnackbarContentLayout)
                 LayoutInflater.from(parent.getContext()).inflate(com.google.android.material.R.layout.design_layout_snackbar_include, parent, false);

         snackbar = constructor.newInstance(context, parent, content, content);
         snackbar.setText(text);
         snackbar.setDuration(duration);
      } catch (Exception e) {
         e.printStackTrace();
      }

      shadowOf(snackbar).text = text.toString();

      shadowSnackbars.add(shadowOf(snackbar));

      return snackbar;
   }

   private static ViewGroup findSuitableParent(View view) {
      ViewGroup fallback = null;
      do {
         if (view instanceof CoordinatorLayout) {
            return (ViewGroup) view;
         } else if (view instanceof FrameLayout) {
            if (view.getId() == android.R.id.content) {
               return (ViewGroup) view;
            } else {
               fallback = (ViewGroup) view;
            }
         }

         if (view != null) {
            final ViewParent parent = view.getParent();

            if (parent == null) {
               fallback = new FrameLayout(view.getContext());
            }

            view = parent instanceof View ? (View) parent : null;
         }
      } while (view != null);

      return fallback;
   }

   @Implementation
   public static Snackbar make(@NonNull View view, @StringRes int resId, int duration) {
      return make(view, view.getResources().getText(resId), duration);
   }


   static ShadowSnackbar shadowOf(Snackbar bar) {
      return Shadow.extract(bar);
   }

   public static void reset() {
      shadowSnackbars.clear();
   }

   public static int shownSnackbarCount() {
      return shadowSnackbars.isEmpty() ? 0 : shadowSnackbars.size();

   }

   public static String getTextOfLatestSnackbar() {
      if (!shadowSnackbars.isEmpty())
         return shadowSnackbars.get(shadowSnackbars.size() - 1).text;

      return null;
   }

   public static Snackbar getLatestSnackbar() {
      if (!shadowSnackbars.isEmpty())
         return shadowSnackbars.get(shadowSnackbars.size() - 1).snackbar;

      return null;
   }
}