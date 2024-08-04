package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.measurement_category
import diaguard.shared.generated.resources.measurement_category_delete
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementCategoryFormScreen(val categoryId: Long) : Screen {

    constructor(category: MeasurementCategory.Local) : this(categoryId = category.id)

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.measurement_category))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = viewModel<MeasurementCategoryFormViewModel> { parametersOf(categoryId) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.measurement_category_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementCategoryFormIntent.DeleteCategory) },
                )
            },
            floatingActionButton = {
                val viewModel = viewModel<MeasurementCategoryFormViewModel> { parametersOf(categoryId) }
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
        MeasurementCategoryForm(viewModel = viewModel { parametersOf(categoryId) })
    }
}