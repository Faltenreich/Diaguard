package com.faltenreich.diaguard.entry.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.food.search.FoodSelectionEvent
import com.faltenreich.diaguard.food.search.FoodSelectionViewModel
import com.faltenreich.diaguard.injection.sharedViewModel
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.entry
import com.faltenreich.diaguard.resource.entry_delete
import com.faltenreich.diaguard.resource.ic_check
import com.faltenreich.diaguard.resource.ic_clear
import com.faltenreich.diaguard.resource.ic_delete
import com.faltenreich.diaguard.resource.save
import com.faltenreich.diaguard.view.bar.BottomAppBarItem
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import com.faltenreich.diaguard.view.theme.AppTheme
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class EntryFormScreen(
    private val entryId: Long,
    private val dateTimeIsoString: String?,
    private val foodId: Long,
) : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.entry))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<EntryFormViewModel>(
            parameters = {
                parametersOf(
                    entryId.takeIf { it >= 0 },
                    dateTimeIsoString,
                    foodId.takeIf { it >= 0 })
            },
        )
        val hasError = viewModel.collectState()?.hasError == true

        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.entry_delete),
                    onClick = { viewModel.dispatchIntent(EntryFormIntent.Delete(needsConfirmation = true)) },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(
                        if (hasError) Res.drawable.ic_clear
                        else Res.drawable.ic_check
                    ),
                    contentDescription = stringResource(Res.string.save),
                    onClick = { viewModel.dispatchIntent(EntryFormIntent.Submit) },
                    containerColor =
                        if (hasError) AppTheme.colors.scheme.errorContainer
                        else AppTheme.colors.scheme.onPrimary,
                    contentColor =
                        if (hasError) AppTheme.colors.scheme.onErrorContainer
                        else AppTheme.colors.scheme.primary,
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<EntryFormViewModel>(
            parameters = {
                parametersOf(
                    entryId.takeIf { it >= 0 },
                    dateTimeIsoString,
                    foodId.takeIf { it >= 0 })
            },
        )

        val foodSelectionViewModel = sharedViewModel<FoodSelectionViewModel>()
        LaunchedEffect(Unit) {
            foodSelectionViewModel.collectEvents { event ->
                when (event) {
                    is FoodSelectionEvent.Select ->
                        viewModel.dispatchIntent(EntryFormIntent.AddFood(event.food))
                }
            }
        }

        EntryForm(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}