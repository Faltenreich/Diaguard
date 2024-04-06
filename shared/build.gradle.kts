plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
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
                implementation(compose.components.resources)
                implementation(compose.foundation)
                implementation(compose.material3)
                // FIXME: Not yet compatible with iOS
                // implementation(compose.preview)
                implementation(compose.runtime)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.dateTime)
                implementation(libs.kotlinx.io)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.csv)
                implementation(libs.kotlinx.serialization.yaml)
                implementation(libs.ktor.core)
                implementation(libs.ktor.contentnegotiation)
                implementation(libs.ktor.serialization)
                implementation(libs.paging.common)
                implementation(libs.paging.common.compose)
                implementation(libs.shimmer)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.bundles.voyager)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.koin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.mockative)
                implementation(libs.turbine)
                implementation(libs.ktor.mock)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.activity.compose)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.emojipicker)
                implementation(libs.androidx.lifecycle.compose)
                implementation(libs.koin.android)
                implementation(libs.ktor.android)
                implementation(libs.paging.android)
                implementation(libs.paging.android.compose)
                implementation(libs.sqldelight.android)
            }
        }
        iosMain
    }

    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
                optIn("androidx.compose.foundation.layout.ExperimentalLayoutApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.ui.text.ExperimentalTextApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.FlowPreview")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    // Workaround: Cannot locate tasks that match ':shared:testClasses'
    // as task 'testClasses' not found in project ':shared'.
    task("testClasses")
}

android {
    namespace = Constants.NameSpace
    compileSdk = Constants.CompileSdk
    defaultConfig {
        minSdk = Constants.MinSdk
    }

    compileOptions {
        sourceCompatibility = Constants.JavaVersion
        targetCompatibility = Constants.JavaVersion
    }
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach { add(it.name, libs.mockative.processor) }
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            packageName.set("${Constants.NameSpace}.shared.database.sqldelight")
        }
    }
}