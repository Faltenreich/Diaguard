package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.detail.TagDetail
import com.faltenreich.diaguard.tag.detail.TagDetailIntent
import com.faltenreich.diaguard.tag.detail.TagDetailViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class TagDetailScreen(private val tag: Tag) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.tag))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TagDetailViewModel> { parametersOf(tag) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.tag_delete,
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.DeleteTag) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<TagDetailViewModel> { parametersOf(tag) }
                FloatingActionButton(onClick = { viewModel.dispatchIntent(TagDetailIntent.UpdateTag) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        TagDetail(viewModel = getViewModel { parametersOf(tag) })
    }
}