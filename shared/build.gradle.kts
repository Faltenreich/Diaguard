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
                implementation(libs.shimmer)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)
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
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.emojipicker)
                implementation(libs.koin.android)
                implementation(libs.ktor.android)
                implementation(libs.paging.android)
                implementation(libs.paging.android.compose)
                implementation(libs.sqldelight.android)
                implementation(libs.voyager.android)
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
    namespace = libs.versions.app.android.namespace.get()
    compileSdk = libs.versions.app.android.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.app.android.sdk.min.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // FIXME
    //  Fixes missing resources on JVM (should be fixed with moko-resources:0.23.1)
    //  https://github.com/icerockdev/moko-resources/issues/510
    sourceSets {
        getByName("main").java.srcDirs("build/generated/moko/androidMain/src")
    }
}

dependencies {
    commonMainApi(libs.moko.resources)
    commonMainApi(libs.moko.resources.compose)
    commonTestImplementation(libs.moko.resources.test)
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach { add(it.name, libs.mockative.processor) }
}

multiplatformResources {
    multiplatformResourcesPackage = libs.versions.app.android.namespace.get()
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            packageName.set("${libs.versions.app.android.namespace.get()}.shared.database.sqldelight")
        }
    }
}