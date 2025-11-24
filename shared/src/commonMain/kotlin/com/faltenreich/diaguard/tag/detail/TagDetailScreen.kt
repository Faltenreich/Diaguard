package com.faltenreich.diaguard.tag.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import diaguard.core.view.generated.resources.ic_check
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.save
import diaguard.shared.generated.resources.tag
import diaguard.shared.generated.resources.tag_delete
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class TagDetailScreen(private val tagId: Long) :
    com.faltenreich.diaguard.navigation.screen.Screen {

    constructor(tag: Tag.Local) : this(tag.id)

    @Composable
    override fun TopAppBar(): com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle {
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.tag))
        }
    }

    @Composable
    override fun BottomAppBar(): com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle {
        val viewModel = viewModel<TagDetailViewModel> { parametersOf(tagId) }
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle.Visible(
            actions = {
                _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.tag_delete),
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.DeleteTag) },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_check),
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