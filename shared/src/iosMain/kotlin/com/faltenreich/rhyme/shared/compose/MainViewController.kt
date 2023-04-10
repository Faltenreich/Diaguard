package com.faltenreich.rhyme.shared.compose

import androidx.compose.ui.window.Application
import com.faltenreich.rhyme.MR
import com.faltenreich.rhyme.MainView
import com.faltenreich.rhyme.shared.di.inject
import com.faltenreich.rhyme.shared.localization.Localization
import platform.UIKit.UIViewController

fun MainViewController(localization: Localization = inject()): UIViewController {
    return Application(localization.getString(MR.strings.app_name)) {
        MainView()
    }
}