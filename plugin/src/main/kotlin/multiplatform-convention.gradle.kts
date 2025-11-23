val libs = extensions.getByType<org.gradle.accessors.dm.LibrariesForLibs>()

plugins {
    kotlin("multiplatform")
    id("core-convention")
    id("com.android.library")
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.viewmodel)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
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

    val javaVersion: Int by rootProject.extra
    jvmToolchain(javaVersion)
}

android {
    val appNamespace: String by rootProject.extra
    val appCompileSdk: Int by rootProject.extra
    namespace = appNamespace
    compileSdk = appCompileSdk
}