package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class EntryFormScreen(
    val entry: Entry? = null,
    val date: Date? = null,
    val food: Food? = null,
) : Screen {

    @Composable
    override fun Content() {
        // TODO: Pass food
        EntryForm(viewModel = getViewModel { parametersOf(entry, date) })
    }
}