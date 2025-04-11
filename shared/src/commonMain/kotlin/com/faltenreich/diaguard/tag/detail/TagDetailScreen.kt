package com.faltenreich.diaguard.tag.detail

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.tag.Tag
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.save
import diaguard.shared.generated.resources.tag
import diaguard.shared.generated.resources.tag_delete
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class TagDetailScreen(private val tagId: Long) : Screen {

    constructor(tag: Tag.Local) : this(tag.id)

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.tag))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<TagDetailViewModel> { parametersOf(tagId) }
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.tag_delete,
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.DeleteTag) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.dispatchIntent(TagDetailIntent.UpdateTag) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            }
        )
    }

    @Composable
    override fun Content(modifier: Modifier) {
        TagDetail(viewModel = viewModel { parametersOf(tagId) })
    }
}