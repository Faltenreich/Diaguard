package com.faltenreich.diaguard

import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.database.sqldelight.sqlDelightModule
import com.faltenreich.diaguard.shared.datetime.dateTimeModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

object DependencyInjection {

    fun setup(declaration: KoinAppDeclaration = {}) {
        startKoin {
            declaration()
            modules(mainModules())
        }
    }
}

private fun mainModules() = listOf(
    DomainModule().module,
    coroutineModule(),
    clipboardModule(),
    dateTimeModule(),
    sqlDelightModule(),
)

@Module
@ComponentScan
class DomainModule