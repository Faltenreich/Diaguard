package com.faltenreich.diaguard.core.di

import android.app.Activity
import org.koin.core.error.NoDefinitionFoundException
import org.koin.core.scope.Scope

fun Scope.androidActivity(): Activity = try {
    get()
} catch (exception: NoDefinitionFoundException) {
    throw IllegalStateException("Can't resolve Activity instance", exception)
}