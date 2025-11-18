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

    compileOptions {
        // Support for java.time on API 25 and older
        isCoreLibraryDesugaringEnabled = true
    }

    packagingOptions.resources.excludes += "META-INF/versions/9/previous-compilation-data.bin"
}

kotlin {
    jvmToolchain(javaVersion)
}

dependencies {
    implementation(project(":shared"))

    coreLibraryDesugaring(libs.android.desugar)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.junit.ktx)
    androidTestImplementation(platform(libs.koin.bom))
    androidTestImplementation(libs.koin.test)

    implementation(libs.activity.compose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
}