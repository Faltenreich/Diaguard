package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.ic_add
import com.faltenreich.diaguard.resource.measurement_categories
import com.faltenreich.diaguard.resource.measurement_category_new
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
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
                    painter = painterResource(Res.drawable.ic_add),
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