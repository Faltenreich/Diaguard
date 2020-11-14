package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.faltenreich.diaguard.shared.view.ViewUtils;

public interface FragmentNavigator {

    enum Operation {
        ADD,
        REPLACE
    }

    default void openFragment(
        @NonNull Fragment fragment,
        @NonNull FragmentManager fragmentManager,
        @IdRes int containerResId,
        Operation operation,
        boolean addToBackStack
    ) {
        Fragment activeFragment = fragmentManager.findFragmentById(containerResId);
        boolean isActive = activeFragment != null && activeFragment.getClass() == fragment.getClass();

        if (!isActive) {
            ViewUtils.hideKeyboard(fragment.getActivity());

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
}
