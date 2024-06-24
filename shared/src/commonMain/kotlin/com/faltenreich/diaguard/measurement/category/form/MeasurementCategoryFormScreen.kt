package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.measurement_category
import diaguard.shared.generated.resources.measurement_category_delete
import diaguard.shared.generated.resources.save
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class MeasurementCategoryFormScreen(val category: MeasurementCategory.Local) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.measurement_category))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementCategoryFormViewModel> { parametersOf(category) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.measurement_category_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementCategoryFormIntent.DeleteCategory) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementCategoryFormViewModel> { parametersOf(category) }
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(MeasurementCategoryFormIntent.UpdateCategory) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            },
        )

    @Composable
    override fun Content() {
        MeasurementCategoryForm(viewModel = getViewModel { parametersOf(category) })
    }
}