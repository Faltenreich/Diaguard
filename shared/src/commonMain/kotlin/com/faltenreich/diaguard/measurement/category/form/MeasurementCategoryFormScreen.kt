package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.ic_check
import com.faltenreich.diaguard.resource.ic_delete
import com.faltenreich.diaguard.resource.measurement_category
import com.faltenreich.diaguard.resource.measurement_category_delete
import com.faltenreich.diaguard.resource.save
import com.faltenreich.diaguard.view.bar.BottomAppBarItem
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementCategoryFormScreen(private val categoryId: Long) : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.measurement_category))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<MeasurementCategoryFormViewModel> { parametersOf(categoryId) }
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
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
                    painter = painterResource(Res.drawable.ic_check),
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