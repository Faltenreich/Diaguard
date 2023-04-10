package com.faltenreich.rhyme.shared.di

import com.faltenreich.rhyme.mainModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object DependencyInjection {

    fun setup(declaration: KoinAppDeclaration = {}) {
        startKoin {
            declaration()
            modules(mainModule())
        }
    }
}