plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.faltenreich.diaguard"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.faltenreich.diaguard"
        minSdk = 21
        targetSdk = 33
        versionCode = 58
        versionName = "4.0.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.4.5"
    packagingOptions.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {
    implementation(project(":shared"))
    implementation(Dependencies.Androidx.activityCompose)
    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.android)
}