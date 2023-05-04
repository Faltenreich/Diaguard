package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun BottomAppBarItem(
    image: ImageVector,
    contentDescription: StringResource,
    onClick: () -> Unit,
){
    IconButton(onClick = onClick) {
        Icon(image, stringResource(contentDescription))
    }
}