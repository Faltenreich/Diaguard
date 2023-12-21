package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.timeline.Timeline
import org.koin.core.parameter.parametersOf

data class TimelineScreen(val date: Date? = null) : Screen {

    @Composable
    override fun Content() {
        Timeline(viewModel = getViewModel { parametersOf(date) })
    }
}