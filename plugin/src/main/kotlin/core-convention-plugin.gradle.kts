val libs = extensions.getByType<org.gradle.accessors.dm.LibrariesForLibs>()

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                // implementation(compose.foundation)
                // implementation(compose.components.resources)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.viewmodel)
            }
        }

        commonTest

        androidMain

        iosMain
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    // jvmToolchain(Constants.JavaVersion)
}

android {
    // namespace = Constants.NameSpace
    // compileSdk = Constants.CompileSdk
}