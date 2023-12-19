package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen
import kotlin.reflect.KClass

class IsNavigatedToUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(clazz: KClass<out Screen>): Boolean {
        val lastItem = navigation.lastItem ?: return false
        return lastItem::class == clazz
    }
}