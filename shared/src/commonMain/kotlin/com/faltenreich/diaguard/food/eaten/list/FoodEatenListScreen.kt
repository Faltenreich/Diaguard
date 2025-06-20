package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.food_eaten
import diaguard.shared.generated.resources.ic_add
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class FoodEatenListScreen(private val foodId: Long) : Screen {

    constructor(tag: Food.Local) : this(tag.id)

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        val viewModel = viewModel<FoodEatenListViewModel> { parametersOf(foodId) }
        return TopAppBarStyle.CenterAligned {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(getString(Res.string.food_eaten))
                Text(
                    text = viewModel.food.name,
                    style = AppTheme.typography.bodySmall,
                )
            }
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<FoodEatenListViewModel> { parametersOf(foodId) }
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodEatenListIntent.CreateEntry) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.entry_new_description),
                    )
                }
            },
        )
    }

    @Composable
    override fun Content() {
        FoodEatenList(viewModel = viewModel { parametersOf(foodId) })
    }
}