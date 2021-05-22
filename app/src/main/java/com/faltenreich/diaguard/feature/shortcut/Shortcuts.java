package com.faltenreich.diaguard.feature.shortcut;

import android.content.Intent;

import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.navigation.Navigating;

public class Shortcuts {

    public static boolean handleShortcut(Navigating navigating, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            Shortcut shortcut = Shortcut.forAction(action);
            if (shortcut == Shortcut.CREATE_ENTRY) {
                navigating.openFragment(new EntryEditFragment(), true);
                return true;
            }
        }
        return false;
    }
}
