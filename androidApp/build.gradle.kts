@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = Configuration.Project.nameSpace
    compileSdk = Configuration.Android.compileSdk

    defaultConfig {
        // TODO: Introduce flavors
        applicationId = "${Configuration.Project.nameSpace}.beta"
        minSdk = Configuration.Android.minSdk
        targetSdk = Configuration.Android.targetSdk
        versionCode = Configuration.Project.versionCode
        versionName = Configuration.Project.versionName
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