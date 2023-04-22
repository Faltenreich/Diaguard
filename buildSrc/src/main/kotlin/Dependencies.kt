object Versions {

    const val androidCompose = "1.7.0"
    const val androidGradle = "8.0.0"
    const val compose = "1.4.0-rc03"
    const val coroutines = "1.6.4"
    const val dateTime = "0.4.0"
    const val koin = "3.4.0"
    const val koinAnnotations = "1.2.0"
    const val kotlin = "1.8.20"
    const val ksp = "1.8.20-1.0.11"
    const val ktor = "2.2.4"
    const val moko = "0.21.1"
    const val serialization = "1.5.0"
    const val turbine = "0.12.3"
}

object Dependencies {

    object Androidx {

        const val activityCompose = "androidx.activity:activity-compose:${Versions.androidCompose}"
    }

    object Koin {

        const val android = "io.insert-koin:koin-android:${Versions.koin}"
        const val annotations = "io.insert-koin:koin-annotations:${Versions.koinAnnotations}"
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val kspCompiler = "io.insert-koin:koin-ksp-compiler:${Versions.koinAnnotations}"
        const val ktor = "io.insert-koin:koin-ktor:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val testJunit4 = "io.insert-koin:koin-test-junit4:${Versions.koin}"
    }

    object Kotlinx {

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.dateTime}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
    }

    object Ktor {

        const val android = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
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