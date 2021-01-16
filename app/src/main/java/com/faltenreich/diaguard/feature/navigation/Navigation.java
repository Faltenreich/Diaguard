package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        @NonNull Operation operation,
        boolean addToBackStack
    ) {
        Fragment currentFragment = getCurrentFragment(fragmentManager, containerResId);
        boolean isActive = currentFragment != null && currentFragment.getClass() == fragment.getClass();

        if (!isActive) {
            String tag = fragment.getClass().getSimpleName();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (operation) {
                case ADD:
                    transaction.add(containerResId, fragment, tag);
                    break;
                case REPLACE:
                    transaction.replace(containerResId, fragment, tag);
                    break;
            }

            if (addToBackStack) {
                transaction.addToBackStack(tag);
            }

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        }
    }

    public static void clearBackStack(@NonNull FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }
}
