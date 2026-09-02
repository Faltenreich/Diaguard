val libs = extensions.getByType<org.gradle.accessors.dm.LibrariesForLibs>()

plugins {
    kotlin("multiplatform")
    id("core-convention")
    id("com.android.kotlin.multiplatform.library")
}

val appNamespace = rootProject.extra["appNamespace"] as String
val appMinSdk = rootProject.extra["appMinSdk"] as Int
val appCompileSdk = rootProject.extra["appCompileSdk"] as Int
val javaVersion = rootProject.extra["javaVersion"] as Int

kotlin {
    android {
        namespace = appNamespace + project.path.replace(":", ".")
        compileSdk = appCompileSdk
        minSdk = appMinSdk

        androidResources {
            enable = true
        }

        withHostTest {
            isIncludeAndroidResources = true
        }

        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            execution = "HOST"
        }
    }
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
                implementation(libs.coroutines.test)
                implementation(libs.turbine)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.coroutines.android)
            }
        }
        getByName("androidHostTest") {
            dependencies {
                implementation(libs.junit)
                implementation(libs.android.test.runner)
                implementation(libs.android.test.junit)
                implementation(libs.android.test.junit.ktx)
                implementation(libs.coroutines.test)
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

    jvmToolchain(javaVersion)
}