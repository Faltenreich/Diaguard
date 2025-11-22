package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.view.TooltipFloatingActionButton
import diaguard.core.view.generated.resources.ic_add
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_categories
import diaguard.shared.generated.resources.measurement_category_new
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data object MeasurementCategoryListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.measurement_categories))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<MeasurementCategoryListViewModel>()
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.measurement_category_new),
                    onClick = { viewModel.dispatchIntent(MeasurementCategoryListIntent.OpenFormDialog) },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<MeasurementCategoryListViewModel>()
        MeasurementCategoryList(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}