package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class LogScreen(val date: Date? = null) : Screen() {

    @Composable
    override fun Content() {
        Log(viewModel = getViewModel { parametersOf(date) })
    }
}