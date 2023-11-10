@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = libs.versions.app.android.namespace.get()
    compileSdk = libs.versions.app.android.sdk.compile.get().toInt()

    defaultConfig {
        // TODO: Introduce flavors
        applicationId = "${libs.versions.app.android.namespace.get()}.beta"
        minSdk = libs.versions.app.android.sdk.min.get().toInt()
        targetSdk = libs.versions.app.android.sdk.target.get().toInt()
        versionCode = libs.versions.app.version.code.get().toInt()
        versionName = libs.versions.app.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    packagingOptions.resources.excludes += "META-INF/versions/9/previous-compilation-data.bin"
}

dependencies {
    implementation(project(":shared"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.junit.ktx)

    implementation(libs.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}