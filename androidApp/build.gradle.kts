@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = Configuration.Project.nameSpace
    compileSdk = Configuration.Android.compileSdk

    defaultConfig {
        applicationId = "${Configuration.Project.nameSpace}.beta"
        minSdk = Configuration.Android.minSdk
        targetSdk = Configuration.Android.targetSdk
        versionCode = Configuration.Project.versionCode
        versionName = Configuration.Project.versionName
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
    composeOptions.kotlinCompilerExtensionVersion = Versions.composeCompiler
    packagingOptions.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {
    implementation(project(":shared"))
    implementation(Dependencies.Androidx.activityCompose)
    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.android)
}