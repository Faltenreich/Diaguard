package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.detail.TagDetail
import com.faltenreich.diaguard.tag.detail.TagDetailIntent
import com.faltenreich.diaguard.tag.detail.TagDetailViewModel
import dev.icerock.moko.resources.compose.painterResource
import org.koin.core.parameter.parametersOf

data class TagDetailScreen(private val tag: Tag) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.tag))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TagDetailViewModel> { parametersOf(tag) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.tag_delete,
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.DeleteTag(tag)) },
                )
            },
        )

    @Composable
    override fun Content() {
        TagDetail(viewModel = getViewModel { parametersOf(tag) })
    }
}