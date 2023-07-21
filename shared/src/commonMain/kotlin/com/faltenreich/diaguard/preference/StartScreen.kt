package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.item.SelectablePreference

enum class StartScreen(override val stableId: Int) : SelectablePreference {
    DASHBOARD(stableId = 0),
    TIMELINE(stableId = 1),
    LOG(stableId = 2),
    ;

    companion object {

        fun valueOf(stableId: Int): StartScreen {
            return values().first { it.stableId == stableId }
        }
    }
}