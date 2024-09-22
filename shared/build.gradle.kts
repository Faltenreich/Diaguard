plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
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
                // TODO: Support multiplatform
                //  https://youtrack.jetbrains.com/issue/CMP-2045
                //  implementation(compose.preview)
                implementation(compose.runtime)
                implementation(libs.compose.navigation)
                implementation(libs.datastore)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.core)
                implementation(libs.koin.viewmodel)
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
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.mock)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.activity.compose)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.emojipicker)
                implementation(libs.androidx.lifecycle.compose)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.android)
                implementation(libs.ktor.android)
                implementation(libs.paging.android)
                implementation(libs.paging.android.compose)
                implementation(libs.sqldelight.android)
            }
        }
        val androidUnitTest by getting
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.junit.ktx)
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
                optIn("androidx.paging.ExperimentalPagingApi")
            }
        }
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    compileOptions {
        sourceCompatibility = Constants.JavaVersion
        targetCompatibility = Constants.JavaVersion
    }
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            packageName.set("${Constants.NameSpace}.shared.database.sqldelight")
        }
    }
}