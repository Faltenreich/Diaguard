package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import diaguard.core.view.generated.resources.ic_check
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.measurement_category
import diaguard.shared.generated.resources.measurement_category_delete
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementCategoryFormScreen(val categoryId: Long) :
    com.faltenreich.diaguard.navigation.screen.Screen {

    constructor(category: MeasurementCategory.Local) : this(categoryId = category.id)

    @Composable
    override fun TopAppBar(): com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle {
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.measurement_category))
        }
    }

    @Composable
    override fun BottomAppBar(): com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle {
        val viewModel = viewModel<MeasurementCategoryFormViewModel> { parametersOf(categoryId) }
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle.Visible(
            actions = {
                _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.measurement_category_delete),
                    onClick = {
                        viewModel.dispatchIntent(
                            MeasurementCategoryFormIntent.Delete(
                                needsConfirmation = true
                            )
                        )
                    },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_check),
                    contentDescription = stringResource(Res.string.save),
                    onClick = { viewModel.dispatchIntent(MeasurementCategoryFormIntent.Store) },
                )
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<MeasurementCategoryFormViewModel> { parametersOf(categoryId) }
        MeasurementCategoryForm(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}