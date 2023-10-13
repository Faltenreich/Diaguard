import org.gradle.api.JavaVersion

object Versions {

    const val androidCompose = "1.7.2"
    const val androidGradle = "8.1.1"
    const val compose = "1.5.10-beta01"
    const val composeCompiler = "1.5.3"
    const val composeMaterial3 = "1.2.0-alpha07"
    const val coroutines = "1.6.4"
    const val dataStore = "1.0.0"
    const val dateTime = "0.4.0"
    const val emojiPicker = "1.4.0"
    const val io = "0.3.0"
    const val koin = "3.4.3"
    const val kotlin = "1.9.10"
    const val ksp = "1.9.0-1.0.13"
    const val ktor = "2.2.4"
    const val mockative = "2.0.1"
    const val moko = "0.23.0"
    const val paging = "3.2.0-alpha05-0.2.3"
    const val pagingAndroid = "3.2.0"
    const val serialization = "1.6.0"
    const val serializationYaml = "0.13.0"
    const val sqlDelight = "2.0.0"
    const val turbine = "0.12.3"
    const val voyager = "1.0.0-rc07"
}

object Configuration {

    object Project {

        const val nameSpace = "com.faltenreich.diaguard"
        // TODO: Increment version code
        const val versionCode = 59
        const val versionName = "4.0.0"
    }

    object Java {

        val version = JavaVersion.VERSION_17
    }

    object Android {

        const val compileSdk = 34
        const val minSdk = 21
        const val targetSdk = 34
    }
}

object Dependencies {

    object Androidx {

        const val activityCompose = "androidx.activity:activity-compose:${Versions.androidCompose}"
        const val composeMaterial3 = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
        const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
        const val emojiPicker = "androidx.emoji2:emoji2-emojipicker:${Versions.emojiPicker}"
    }

    object Koin {

        const val android = "io.insert-koin:koin-android:${Versions.koin}"
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val ktor = "io.insert-koin:koin-ktor:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val testJunit4 = "io.insert-koin:koin-test-junit4:${Versions.koin}"
    }

    object Kotlinx {

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.dateTime}"
        const val io = "org.jetbrains.kotlinx:kotlinx-io-core:${Versions.io}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
        const val serializationYaml = "net.mamoe.yamlkt:yamlkt:${Versions.serializationYaml}"
    }

    object Ktor {

        const val android = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val mock = "io.ktor:ktor-client-mock:${Versions.ktor}"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    }

    object Mockative {

        const val core = "io.mockative:mockative:${Versions.mockative}"
        const val processor = "io.mockative:mockative-processor:${Versions.mockative}"
    }

    object Moko {

        const val resourcesPlugin = "dev.icerock.moko:resources-generator:${Versions.moko}"
        const val resources = "dev.icerock.moko:resources:${Versions.moko}"
        const val resourcesCompose = "dev.icerock.moko:resources-compose:${Versions.moko}"
        const val resourcesTest = "dev.icerock.moko:resources-test:${Versions.moko}"
    }

    object Paging {

        const val common = "app.cash.paging:paging-common:${Versions.paging}"
        const val android = "androidx.paging:paging-runtime:${Versions.pagingAndroid}"
        const val androidCompose = "androidx.paging:paging-compose:${Versions.pagingAndroid}"
    }

    object SqlDelight {

        const val android = "app.cash.sqldelight:android-driver:${Versions.sqlDelight}"
        const val coroutines = "app.cash.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    }

    object Turbine {

        const val core = "app.cash.turbine:turbine:${Versions.turbine}"
    }

    object Voyager {

        const val android = "cafe.adriel.voyager:voyager-koin:${Versions.voyager}"
        const val navigator = "cafe.adriel.voyager:voyager-navigator:${Versions.voyager}"
        const val transitions = "cafe.adriel.voyager:voyager-transitions:${Versions.voyager}"
    }
}