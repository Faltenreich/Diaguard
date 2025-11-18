plugins {
    id("multiplatform-convention")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

val appNamespace: String by rootProject.extra
val appCompileSdk: Int by rootProject.extra
val javaVersion: Int by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.components.resources)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.viewmodel)
            }
        }
    }

    jvmToolchain(javaVersion)
}

android {
    namespace = appNamespace
    compileSdk = appCompileSdk
}