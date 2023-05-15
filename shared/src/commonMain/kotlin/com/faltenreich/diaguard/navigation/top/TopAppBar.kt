package com.faltenreich.diaguard.navigation.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.compose.stringResource

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
                            contentDescription = stringResource(MR.strings.navigate_back),
                        )
                    }
                }
            }
        )
    }
}