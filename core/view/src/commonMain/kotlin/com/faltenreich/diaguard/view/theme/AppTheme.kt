package com.faltenreich.diaguard.view.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faltenreich.diaguard.view.theme.color.ColorSchemes
import com.faltenreich.diaguard.view.theme.color.Colors
import com.faltenreich.diaguard.view.theme.color.LocalColors
import com.faltenreich.diaguard.view.theme.color.animated
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppTheme(
    isDarkColorScheme: Boolean,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkColorScheme) ColorSchemes.dark else ColorSchemes.light

    MaterialTheme(
        colorScheme = colorScheme.animated(),
        typography = Typography(
            headlineSmall = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp
            ),
            titleLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            ),
            bodyLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp
            ),
            bodyMedium = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            ),
            labelMedium = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp
            )

        ),
        shapes = Shapes(
            extraSmall = RoundedCornerShape(4.dp),
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(16.dp),
            extraLarge = RoundedCornerShape(28.dp),
        ),
        content = content,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme(isDarkColorScheme = true) {
        Text("AppTheme")
    }
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