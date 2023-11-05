plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.google.devtools.ksp")
    id("app.cash.sqldelight")
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                // FIXME: https://github.com/JetBrains/compose-multiplatform/issues/2914
                // implementation(compose.preview)
                implementation(compose.runtime)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Kotlinx.coroutines)
                implementation(Dependencies.Kotlinx.dateTime)
                implementation(Dependencies.Kotlinx.io)
                implementation(Dependencies.Kotlinx.serialization)
                implementation(Dependencies.Kotlinx.serializationCsv)
                implementation(Dependencies.Kotlinx.serializationYaml)
                implementation(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.contentNegotiation)
                implementation(Dependencies.Ktor.serialization)
                implementation(Dependencies.Paging.common)
                implementation(Dependencies.Shimmer.core)
                implementation(Dependencies.SqlDelight.coroutines)
                implementation(Dependencies.Voyager.navigator)
                implementation(Dependencies.Voyager.transitions)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(Dependencies.Koin.test)
                implementation(Dependencies.Kotlinx.coroutinesTest)
                implementation(Dependencies.Mockative.core)
                implementation(Dependencies.Turbine.core)
                implementation(Dependencies.Ktor.mock)
            }
        }
        androidMain {
            dependencies {
                implementation(Dependencies.Androidx.composeMaterial3)
                implementation(Dependencies.Androidx.dataStore)
                implementation(Dependencies.Androidx.emojiPicker)
                implementation(Dependencies.Koin.android)
                implementation(Dependencies.Ktor.android)
                implementation(Dependencies.Paging.android)
                implementation(Dependencies.Paging.androidCompose)
                implementation(Dependencies.SqlDelight.android)
                implementation(Dependencies.Voyager.android)
            }
        }
        iosMain
    }

    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.ui.text.ExperimentalTextApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.FlowPreview")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }
    }
}

android {
    namespace = Configuration.Project.nameSpace
    compileSdk = Configuration.Android.compileSdk
    defaultConfig {
        minSdk = Configuration.Android.minSdk
    }

    compileOptions {
        sourceCompatibility = Configuration.Java.version
        targetCompatibility = Configuration.Java.version
    }

    // FIXME
    //  Fixes missing resources on JVM (should be fixed with moko-resources:0.23.1)
    //  https://github.com/icerockdev/moko-resources/issues/510
    sourceSets {
        getByName("main").java.srcDirs("build/generated/moko/androidMain/src")
    }
}

dependencies {
    commonMainApi(Dependencies.Moko.resources)
    commonMainApi(Dependencies.Moko.resourcesCompose)
    commonTestImplementation(Dependencies.Moko.resourcesTest)
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach { add(it.name, Dependencies.Mockative.processor) }
}

multiplatformResources {
    multiplatformResourcesPackage = Configuration.Project.nameSpace
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            packageName.set("${Configuration.Project.nameSpace}.shared.database.sqldelight")
        }
    }
}