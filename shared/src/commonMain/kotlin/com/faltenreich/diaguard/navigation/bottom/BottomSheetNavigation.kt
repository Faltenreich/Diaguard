package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.NavigationIntent
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.ExportFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.PreferenceListScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.StatisticScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.BottomSheetState
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Composable
fun BottomSheetNavigation(
    bottomSheetState: BottomSheetState,
    onDismissRequest: () -> Unit,
    onIntent: (NavigationIntent) -> Unit,
    isActive: (KClass<out Screen>) -> Boolean,
    modifier: Modifier = Modifier,
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = bottomSheetState,
    ) {
        val scope = rememberCoroutineScope()
        val navigateTo = { screen: Screen, clearBackStack: Boolean ->
            scope.launch {
                onIntent(NavigationIntent.NavigateTo(screen, clearBackStack))
                bottomSheetState.hide()
            }.invokeOnCompletion { onDismissRequest() }
        }
        Column {
            BottomSheetNavigationItem(
                label = MR.strings.dashboard,
                icon = MR.images.ic_dashboard,
                isActive = isActive(DashboardScreen::class),
                onClick = { navigateTo(DashboardScreen, true) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.timeline,
                icon = MR.images.ic_timeline,
                isActive = isActive(TimelineScreen::class),
                onClick = { navigateTo(TimelineScreen(), true) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.log,
                icon = MR.images.ic_log,
                isActive = isActive(LogScreen::class),
                onClick = { navigateTo(LogScreen(), true) },
            )
            Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))
            BottomSheetNavigationItem(
                label = MR.strings.food,
                icon = null,
                isActive = isActive(FoodListScreen::class),
                onClick = { navigateTo(FoodListScreen(), false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.statistic,
                icon = null,
                isActive = isActive(StatisticScreen::class),
                onClick = { navigateTo(StatisticScreen, false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.export,
                icon = null,
                isActive = isActive(ExportFormScreen::class),
                onClick = { navigateTo(ExportFormScreen, false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.preferences,
                icon = null,
                isActive = isActive(PreferenceListScreen::class),
                onClick = { navigateTo(PreferenceListScreen(), false) },
            )
        }
    }
}