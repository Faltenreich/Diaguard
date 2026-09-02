plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

val appNamespace = rootProject.extra["appNamespace"] as String
val appVersionCode = rootProject.extra["appVersionCode"] as Int
val appVersionName = rootProject.extra["appVersionName"] as String
val appMinSdk = rootProject.extra["appMinSdk"] as Int
val appTargetSdk = rootProject.extra["appTargetSdk"] as Int
val appCompileSdk = rootProject.extra["appCompileSdk"] as Int
val javaVersion = rootProject.extra["javaVersion"] as Int

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
    implementation(project(":core:system"))
    implementation(project(":app:common"))

    implementation(libs.android.activity.compose)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    coreLibraryDesugaring(libs.android.desugar)
}