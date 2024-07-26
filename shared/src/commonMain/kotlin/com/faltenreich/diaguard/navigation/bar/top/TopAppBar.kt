package com.faltenreich.diaguard.navigation.bar.top

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_arrow_back
import diaguard.shared.generated.resources.navigate_back
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopAppBar(
    style: TopAppBarStyle,
    // TODO: Extract into some sort of ViewModel
    navigation: Navigation = inject(),
) {
    val scope = rememberCoroutineScope()
    when (style) {
        is TopAppBarStyle.Hidden -> Unit
        is TopAppBarStyle.CenterAligned -> CenterAlignedTopAppBar(
            title = { style.content() },
            navigationIcon = {
                if (navigation.canPop()) {
                    IconButton(onClick = { scope.launch { navigation.pop() } }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = getString(Res.string.navigate_back),
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.scheme.primary,
                navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
                titleContentColor = AppTheme.colors.scheme.onPrimary,
                actionIconContentColor = AppTheme.colors.scheme.onPrimary,
            ),
        )
    }
}