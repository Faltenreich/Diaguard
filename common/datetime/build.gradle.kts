plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.kotlinx.dateTime)
            }
        }

        commonTest {
            dependencies {}
        }

        androidMain {
            dependencies {}
        }

        iosMain

        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    jvmToolchain(Constants.JavaVersion)
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
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar)
}