package com.faltenreich.diaguard.entry.form

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.sharedViewModel
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry
import diaguard.shared.generated.resources.entry_delete
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class EntryFormScreen(
    private val entryId: Long,
    private val dateTimeIsoString: String?,
    private val foodId: Long,
) : Screen {

    constructor(
        entry: Entry.Local? = null,
        date: Date? = null,
        food: Food.Local? = null,
    ) : this(
        entryId = entry?.id ?: -1,
        // Attention: Will be converted to DateTime at Time now in GetDateTimeForEntryUseCase
        dateTimeIsoString = date?.atStartOfDay()?.isoString,
        foodId = food?.id ?: -1,
    )

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.entry))
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
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.entry_delete,
                    onClick = { viewModel.dispatchIntent(EntryFormIntent.Delete) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.dispatchIntent(EntryFormIntent.Submit) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
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
        EntryForm(
            viewModel = viewModel,
            foodSearchViewModel = sharedViewModel(parameters = { parametersOf(FoodSearchMode.FIND) }),
        )
    }
}