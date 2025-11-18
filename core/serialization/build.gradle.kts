plugins {
    id("multiplatform-convention")
}

val appNamespace: String by rootProject.extra
val appCompileSdk: Int by rootProject.extra
val javaVersion: Int by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.csv)
                implementation(libs.kotlinx.serialization.yaml)
            }
        }
        all {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }
    }

    jvmToolchain(javaVersion)
}

android {
    namespace = appNamespace
    compileSdk = appCompileSdk
}