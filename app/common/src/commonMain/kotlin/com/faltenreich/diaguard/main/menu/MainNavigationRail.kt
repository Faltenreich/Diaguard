package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
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
    val isSelected = false // TODO
    val onPrimaryColor =
        if (isSelected) AppTheme.colors.scheme.primary
        else AppTheme.colors.scheme.onBackground

    NavigationRail(modifier = modifier.verticalScroll(rememberScrollState())) {
        MainMenuItem.entries.forEach { mainMenuItem ->
            NavigationRailItem(
                selected = isSelected,
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
                            tint = onPrimaryColor,
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(mainMenuItem.label),
                        color = onPrimaryColor,
                    )
                }
            )
        }
    }
}