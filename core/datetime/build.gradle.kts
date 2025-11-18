plugins {
    id("multiplatform-convention")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

val appNamespace: String by rootProject.extra
val appMinSdk: Int by rootProject.extra
val appCompileSdk: Int by rootProject.extra
val javaVersion: Int by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:localization"))
                implementation(project(":core:serialization"))
                implementation(compose.components.resources)
                implementation(compose.foundation)
                implementation(libs.kotlinx.dateTime)
            }
        }
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }
    }

    jvmToolchain(javaVersion)
}

android {
    namespace = appNamespace
    compileSdk = appCompileSdk
    defaultConfig {
        minSdk = appMinSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar)
}