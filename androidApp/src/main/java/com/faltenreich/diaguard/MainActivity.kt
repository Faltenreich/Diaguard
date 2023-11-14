package com.faltenreich.diaguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.faltenreich.diaguard.shared.view.invalidateStatusBarStyle

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        invalidateStatusBarStyle(isDarkColorScheme = true)
        setContent { AppView() }
    }
}