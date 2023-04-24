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
    android()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.preview)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Koin.annotations)
                implementation(Dependencies.Kotlinx.coroutines)
                implementation(Dependencies.Kotlinx.dateTime)
                implementation(Dependencies.Kotlinx.serialization)
                implementation(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.contentNegotiation)
                implementation(Dependencies.Ktor.serialization)
                implementation(Dependencies.SqlDelight.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Dependencies.Koin.test)
                implementation(Dependencies.Kotlinx.coroutinesTest)
                implementation(Dependencies.Turbine.core)
                implementation(Dependencies.Ktor.mock)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Koin.android)
                implementation(Dependencies.Ktor.android)
                implementation(Dependencies.SqlDelight.android)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(Dependencies.Koin.testJunit4)
            }
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.FlowPreview")
            }
        }
    }
}

// TODO: Find way to avoid redundant declarations
android {
    namespace = "com.faltenreich.diaguard"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }

    // Workaround for unresolved reference on Android
    // https://github.com/icerockdev/moko-resources/issues/353
    sourceSets["main"].apply {
        assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    commonMainApi(Dependencies.Moko.resources)
    add("kspCommonMainMetadata", Dependencies.Koin.kspCompiler)
    add("kspAndroid", Dependencies.Koin.kspCompiler)
}

multiplatformResources {
    multiplatformResourcesPackage = "com.faltenreich.diaguard"
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            packageName.set("com.faltenreich.diaguard.shared.database.sqldelight")
        }
    }
}