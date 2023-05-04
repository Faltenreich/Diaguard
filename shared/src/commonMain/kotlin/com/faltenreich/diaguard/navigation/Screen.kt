package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarOwner
import com.faltenreich.diaguard.navigation.top.TopAppBarOwner
import com.faltenreich.diaguard.shared.architecture.ViewModel

typealias VoyagerScreen = cafe.adriel.voyager.core.screen.Screen

interface Screen : VoyagerScreen, TopAppBarOwner, BottomAppBarOwner

@Composable
inline fun <reified VM : ViewModel> Screen.rememberViewModel(
    crossinline factory: @DisallowComposableCalls () -> VM,
): VM {
    return rememberScreenModel(factory = factory)
}