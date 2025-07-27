package com.faltenreich.diaguard.startup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StartupView(
    modifier: Modifier = Modifier,
    viewModel: StartupViewModel = viewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.dispatchIntent(StartupIntent.MigrateData)
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun Preview() {
    StartupView()
}