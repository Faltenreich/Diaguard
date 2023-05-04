package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarOwner
import com.faltenreich.diaguard.navigation.top.TopAppBarOwner
import com.faltenreich.diaguard.shared.architecture.ViewModel

typealias VoyagerScreen = cafe.adriel.voyager.core.screen.Screen

interface Screen<VM : ViewModel> : VoyagerScreen, TopAppBarOwner, BottomAppBarOwner {

    fun createViewModel(): VM
}

@Composable
inline fun <reified VM : ViewModel> Screen<VM>.rememberViewModel(): VM {
    return rememberScreenModel { createViewModel() }
}