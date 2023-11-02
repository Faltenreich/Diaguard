package com.faltenreich.diaguard

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
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
    isDarkColorScheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isDarkColorScheme) ColorSchemes.dark else ColorSchemes.light,
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