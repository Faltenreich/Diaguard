package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainNavigationRail(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (target: NavigationTarget, popHistory: Boolean) -> Unit,
) {
    val currentDestination = navController.currentDestination?.route

    NavigationRail(
        modifier = modifier.verticalScroll(rememberScrollState()),
        containerColor = AppTheme.colors.scheme.primaryContainer,
        contentColor = AppTheme.colors.scheme.onPrimaryContainer,
    ) {
        MainMenuItem.entries.forEachIndexed { index, mainMenuItem ->
            NavigationRailItem(
                // TODO
                selected = index == 0,
                onClick = {
                    onItemClick(
                        mainMenuItem.navigationTarget,
                        mainMenuItem.icon != null,
                    )
                },
                icon = {
                    mainMenuItem.icon?.let { icon ->
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(mainMenuItem.label),
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = AppTheme.colors.scheme.onPrimaryContainer,
                    selectedTextColor = AppTheme.colors.scheme.onPrimaryContainer,
                    indicatorColor = AppTheme.colors.scheme.secondaryContainer,
                    unselectedIconColor = AppTheme.colors.scheme.onPrimaryContainer,
                    unselectedTextColor = AppTheme.colors.scheme.onPrimaryContainer,
                    disabledIconColor = AppTheme.colors.scheme.onPrimaryContainer,
                    disabledTextColor = AppTheme.colors.scheme.onPrimaryContainer,
                ),
            )
        }
    }
}