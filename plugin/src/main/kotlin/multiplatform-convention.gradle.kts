val libs = extensions.getByType<org.gradle.accessors.dm.LibrariesForLibs>()

plugins {
    id("core-convention")
    id("com.android.library")
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}

android {
    val appNamespace: String by rootProject.extra
    val appCompileSdk: Int by rootProject.extra
    namespace = appNamespace
    compileSdk = appCompileSdk
}