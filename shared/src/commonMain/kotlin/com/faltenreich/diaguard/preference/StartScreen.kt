package com.faltenreich.diaguard.preference

enum class StartScreen(val stableId: Int) {
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