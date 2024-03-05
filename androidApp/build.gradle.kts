plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = Constants.NameSpace
    compileSdk = Constants.CompileSdk

    defaultConfig {
        // TODO: Introduce flavors
        applicationId = "${Constants.NameSpace}.beta"
        minSdk = Constants.MinSdk
        targetSdk = Constants.TargetSdk
        versionCode = Constants.VersionCode
        versionName = Constants.VersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = Constants.JavaVersion
        targetCompatibility = Constants.JavaVersion
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

    implementation(libs.activity.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}