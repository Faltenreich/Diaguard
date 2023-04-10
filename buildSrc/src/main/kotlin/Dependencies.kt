object Versions {

    const val androidCompose = "1.6.1"
    const val androidGradle = "7.3.1"
    const val compose = "1.4.0-rc03"
    const val coroutines = "1.6.4"
    const val koin = "3.2.2"
    const val koinAnnotations = "1.0.3"
    const val kotlin = "1.8.10"
    const val ksp = "1.8.10-1.0.9"
    const val ktor = "2.1.3"
    const val moko = "0.20.1"
    const val serialization = "1.4.1"
    const val turbine = "0.12.1"
}

object Dependencies {

    object Androidx {

        const val activityCompose = "androidx.activity:activity-compose:${Versions.androidCompose}"
    }

    object Koin {

        const val annotations = "io.insert-koin:koin-annotations:${Versions.koinAnnotations}"
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val kspCompiler = "io.insert-koin:koin-ksp-compiler:${Versions.koinAnnotations}"
        const val ktor = "io.insert-koin:koin-ktor:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val testJunit4 = "io.insert-koin:koin-test-junit4:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
    }

    object Kotlinx {

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
    }

    object Ktor {

        const val android = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val ios = "io.ktor:ktor-client-darwin:${Versions.ktor}"
        const val mock = "io.ktor:ktor-client-mock:${Versions.ktor}"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    }

    object Moko {

        const val resourcesPlugin = "dev.icerock.moko:resources-generator:${Versions.moko}"
        const val resources = "dev.icerock.moko:resources:${Versions.moko}"
    }

    object Turbine {

        const val core = "app.cash.turbine:turbine:${Versions.turbine}"
    }
}