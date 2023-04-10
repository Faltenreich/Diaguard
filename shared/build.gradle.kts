plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.google.devtools.ksp")
}

kotlin {
    android()
    ios()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                @Suppress("OPT_IN_IS_NOT_ENABLED")
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(compose.runtime)
                // FIXME: Not supported on iOS yet
                // implementation(compose.preview)
                // implementation("org.jetbrains.compose.ui:ui-test-junit4:")

                implementation(Dependencies.Koin.core)
                implementation(Dependencies.Koin.annotations)
                implementation(Dependencies.Kotlinx.coroutines)
                implementation(Dependencies.Kotlinx.serialization)
                implementation(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.contentNegotiation)
                implementation(Dependencies.Ktor.serialization)
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
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependencies.Koin.testJunit4)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.ios)
            }
        }
        val iosTest by getting {
            dependencies {

            }
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        }
    }
}

android {
    namespace = "com.faltenreich.rhyme"
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
}

dependencies {
    commonMainApi(Dependencies.Moko.resources)
    add("kspCommonMainMetadata", Dependencies.Koin.kspCompiler)
    add("kspAndroid", Dependencies.Koin.kspCompiler)
}

multiplatformResources {
    multiplatformResourcesPackage = "com.faltenreich.rhyme"
}