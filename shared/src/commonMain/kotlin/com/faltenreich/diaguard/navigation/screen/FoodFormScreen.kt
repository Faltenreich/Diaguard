package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.form.FoodForm
import com.faltenreich.diaguard.food.form.FoodFormIntent
import com.faltenreich.diaguard.food.form.FoodFormViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.food_delete
import diaguard.shared.generated.resources.food_eaten
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.ic_history
import diaguard.shared.generated.resources.save
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class FoodFormScreen(private val food: Food.Local? = null) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.food))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<FoodFormViewModel> { parametersOf(food) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.food_delete,
                    onClick = { viewModel.dispatchIntent(FoodFormIntent.Delete) },
                )
                food?.let { food ->
                    BottomAppBarItem(
                        painter = painterResource(Res.drawable.ic_history),
                        contentDescription = Res.string.food_eaten,
                        onClick = { viewModel.dispatchIntent(FoodFormIntent.OpenFoodEaten(food)) },
                    )
                }
            },
            floatingActionButton = {
                val viewModel = getViewModel<FoodFormViewModel> { parametersOf(food) }
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodFormIntent.Submit) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        FoodForm(viewModel = getViewModel { parametersOf(food) })
    }
}