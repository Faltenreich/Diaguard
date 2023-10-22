package com.faltenreich.diaguard

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faltenreich.diaguard.shared.theme.Dimensions
import com.faltenreich.diaguard.shared.theme.LocalDimensions
import com.faltenreich.diaguard.shared.theme.color.ColorSchemes
import com.faltenreich.diaguard.shared.theme.color.Colors
import com.faltenreich.diaguard.shared.theme.color.LocalColors

@Composable
fun AppTheme(
    darkTheme: Boolean = false, // isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) ColorSchemes.dark else ColorSchemes.light,
        typography = Typography(
            bodyMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )
        ),
        shapes = Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(0.dp),
        ),
        content = content,
    )
}

object AppTheme {

    val colors: Colors
        @Composable
        get() = LocalColors.current

    val dimensions: Dimensions
        @Composable
        get() = LocalDimensions.current

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography
}