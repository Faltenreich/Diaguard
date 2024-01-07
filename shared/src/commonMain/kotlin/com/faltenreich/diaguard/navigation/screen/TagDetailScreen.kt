package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.detail.TagDetail
import org.koin.core.parameter.parametersOf

data class TagDetailScreen(private val tag: Tag) : Screen {

    @Composable
    override fun Content() {
        TagDetail(viewModel = getViewModel { parametersOf(tag) })
    }
}