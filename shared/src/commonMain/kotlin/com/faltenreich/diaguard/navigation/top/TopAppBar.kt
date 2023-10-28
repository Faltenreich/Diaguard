package com.faltenreich.diaguard.navigation.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun TopAppBar(
    style: TopAppBarStyle,
    navigator: Navigator,
) {
    when (style) {
        is TopAppBarStyle.Hidden -> Unit
        is TopAppBarStyle.CenterAligned -> CenterAlignedTopAppBar(
            title = { style.content() },
            navigationIcon = {
                if (navigator.canPop) {
                    IconButton(onClick = navigator::pop) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = getString(MR.strings.navigate_back),
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.scheme.surfaceVariant,
            ),
        )
    }
}