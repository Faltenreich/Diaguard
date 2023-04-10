package com.faltenreich.diaguard.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object DependencyInjection {

    fun setup(declaration: KoinAppDeclaration = {}) {
        startKoin {
            declaration()
            modules(com.faltenreich.diaguard.mainModule())
        }
    }
}