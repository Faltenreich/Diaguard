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
                implementation(project(":core:injection"))
                implementation(compose.foundation)
                implementation(compose.components.resources)
            }
        }
    }

    jvmToolchain(javaVersion)
}

android {
    namespace = appNamespace
    compileSdk = appCompileSdk
}