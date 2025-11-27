package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bar.bottom.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.bar.snackbar.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.bar.top.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun navigationModule() = module {
    singleOf(::Navigation)

    factoryOf(::GetTopAppBarStyleUseCase)
    factoryOf(::GetBottomAppBarStyleUseCase)

    factoryOf(::NavigateToUseCase)
    factoryOf(::NavigateBackUseCase)

    factoryOf(::ShowSnackbarUseCase)
}