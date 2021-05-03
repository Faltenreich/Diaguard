package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class Navigation {

    public enum Operation {
        ADD,
        REPLACE
    }

    @Nullable
    public static Fragment getCurrentFragment(
        @NonNull FragmentManager fragmentManager,
        @IdRes int containerResId
    ) {
        return fragmentManager.findFragmentById(containerResId);
    }

    public static void openFragment(
        @NonNull Fragment fragment,
        @NonNull FragmentManager fragmentManager,
        @IdRes int containerResId,
        boolean addToBackStack
    ) {
        Fragment currentFragment = getCurrentFragment(fragmentManager, containerResId);
        boolean isActive = currentFragment != null && currentFragment.getClass() == fragment.getClass();

        if (!isActive) {
            String tag = fragment.getClass().getSimpleName();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(containerResId, fragment, tag);
            if (addToBackStack) {
                transaction.addToBackStack(tag);
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        }
    }

    public static void clearBackStack(@NonNull FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static Fragment instantiateFragment(
        @NonNull Preference preference,
        @NonNull FragmentManager fragmentManager,
        @NonNull ClassLoader classLoader,
        @NonNull PreferenceFragmentCompat caller
    ) {
        FragmentFactory factory = fragmentManager.getFragmentFactory();
        Fragment fragment = factory.instantiate(classLoader, preference.getFragment());
        fragment.setArguments(preference.getExtras());
        fragment.setTargetFragment(caller, 0);
        return fragment;
    }
}
