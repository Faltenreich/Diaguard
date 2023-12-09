package com.faltenreich.diaguard.navigation.top

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun TopAppBar(
    style: TopAppBarStyle,
    navigation: Navigation = inject(),
) {
    when (style) {
        is TopAppBarStyle.Hidden -> Unit
        is TopAppBarStyle.CenterAligned -> CenterAlignedTopAppBar(
            title = { style.content() },
            navigationIcon = {
                if (navigation.canPop()) {
                    IconButton(onClick = navigation::pop) {
                        Icon(
                            painter = painterResource(MR.images.ic_arrow_back),
                            contentDescription = getString(MR.strings.navigate_back),
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