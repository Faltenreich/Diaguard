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
    composeOptions.kotlinCompilerExtensionVersion = Versions.composeCompiler
    packagingOptions.resources.excludes += "META-INF/versions/9/previous-compilation-data.bin"
}

dependencies {
    implementation(project(":shared"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")

    implementation(Dependencies.Androidx.activityCompose)
    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.android)
}