package com.faltenreich.diaguard.shared.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/**
 * Wrapper for dependency injection where constructor injection is not possible
 */
object MethodInjection : KoinComponent {

    /**
     * Resolve dependency that has been injected
     *
     * @param qualifier Optional class qualified for the dependency resolution
     * @param parameters Optional parameters of the resolved dependency
     * @return Resolved dependency
     */
    inline fun <reified T: Any> inject(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T = get(qualifier, parameters)
}

/**
 * Convenience method for [MethodInjection.inject]
 */
inline fun <reified T: Any> inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T = MethodInjection.inject(qualifier, parameters)