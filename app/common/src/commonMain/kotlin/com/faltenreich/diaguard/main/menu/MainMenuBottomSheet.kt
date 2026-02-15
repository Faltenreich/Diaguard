package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.reflect.KClass

@Composable
fun MainMenuBottomSheet(
    navController: NavController,
    onDismissRequest: () -> Unit,
    onItemClick: (target: NavigationTarget, popHistory: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentDestination = navController.currentDestination?.route

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) {
            MainMenuItem.entries.forEach { mainMenuItem ->
                Item(
                    label = stringResource(mainMenuItem.label),
                    icon = mainMenuItem.icon?.let { painterResource(it) },
                    isSelected = false, // TODO
                    onClick = {
                        onItemClick(
                            mainMenuItem.navigationTarget,
                            mainMenuItem.icon != null,
                        )
                    },
                )
            }
            // TODO: Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))
        }
    }
}

@Composable
private fun Item(
    label: String,
    icon: Painter?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.dimensions.padding.P_2,
                vertical = AppTheme.dimensions.padding.P_1,
            )
            .background(
                color =
                    if (isSelected && icon != null) AppTheme.colors.scheme.surfaceContainerLowest
                    else Color.Transparent,
                shape = AppTheme.shapes.large,
            )
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3,
                vertical = AppTheme.dimensions.padding.P_2_5,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val onPrimaryColor =
            if (isSelected) AppTheme.colors.scheme.primary
            else AppTheme.colors.scheme.onBackground
        icon?.let {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
                tint = onPrimaryColor,
            )
        } ?: Spacer(modifier = Modifier.size(AppTheme.dimensions.padding.P_4))
        Text(
            text = label,
            color = onPrimaryColor,
        )
    }
}

private fun String?.isSelecting(kClass: KClass<*>): Boolean {
    val className = kClass.simpleName ?: return false
    return this?.contains(className) ?: false
}

@Preview(showBackground = true)
@Composable
private fun Preview() = PreviewScaffold {
    MainMenuBottomSheet(
        navController = rememberNavController(),
        onDismissRequest = {},
        onItemClick = { _, _ -> },
    )
}