plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
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
        testInstrumentationRunner = "com.faltenreich.diaguard.TestInstrumentationRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
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
    packagingOptions.resources.excludes += "META-INF/versions/9/previous-compilation-data.bin"
}

dependencies {
    implementation(project(":shared"))

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.junit.ktx)
    androidTestImplementation(libs.koin.test)

    implementation(libs.activity.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}