plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

val appNamespace: String by rootProject.extra
val appVersionCode: Int by rootProject.extra
val appVersionName: String by rootProject.extra
val appMinSdk: Int by rootProject.extra
val appTargetSdk: Int by rootProject.extra
val appCompileSdk: Int by rootProject.extra
val javaVersion: Int by rootProject.extra

android {
    namespace = appNamespace
    compileSdk = appCompileSdk

    defaultConfig {
        minSdk = appMinSdk
        targetSdk = appTargetSdk
        versionCode = appVersionCode
        versionName = appVersionName
        testInstrumentationRunner = "$appNamespace.TestInstrumentationRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("demo") {
            applicationId = appNamespace
        }
        create("beta") {
            applicationId = "$appNamespace.beta"
        }
        create("store") {
            applicationId = appNamespace
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    // Support for java.time on API 25 and older
    compileOptions.isCoreLibraryDesugaringEnabled = true
}

kotlin {
    jvmToolchain(javaVersion)
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.activity.compose)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    coreLibraryDesugaring(libs.android.desugar)
}