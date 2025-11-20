package com.faltenreich.diaguard.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberAnimatable(): Animatable<Float, AnimationVector1D> = rememberSaveable(
    saver = Saver(
        save = { it.value },
        restore = { Animatable(initialValue = it) },
    ),
    init = { Animatable(0f) },
)