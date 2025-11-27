package com.faltenreich.diaguard.tag.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.data.navigation.BottomAppBarStyle
import com.faltenreich.diaguard.data.navigation.TopAppBarStyle
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.ic_check
import com.faltenreich.diaguard.resource.ic_delete
import com.faltenreich.diaguard.resource.save
import com.faltenreich.diaguard.resource.tag
import com.faltenreich.diaguard.resource.tag_delete
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class TagDetailScreen(private val tagId: Long) : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.tag))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<TagDetailViewModel> { parametersOf(tagId) }
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.tag_delete),
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.DeleteTag) },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_check),
                    contentDescription = stringResource(Res.string.save),
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.UpdateTag) },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<TagDetailViewModel> { parametersOf(tagId) }
        TagDetail(
            state = viewModel.collectState(),
            entries = viewModel.pagingData.collectAsLazyPagingItems(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}