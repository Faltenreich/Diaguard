package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.measurement_categories
import diaguard.shared.generated.resources.measurement_category_new
import org.jetbrains.compose.resources.painterResource

data object MeasurementCategoryListScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.measurement_categories))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementCategoryListViewModel>()
                FloatingActionButton(
                    onClick = {
                        viewModel.dispatchIntent(MeasurementCategoryListIntent.Create)
                    },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.measurement_category_new),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        MeasurementCategoryList(viewModel = getViewModel())
    }
}