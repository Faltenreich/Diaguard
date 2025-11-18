plugins {
    id("multiplatform-convention")
    alias(libs.plugins.sqldelight)
}

val appNamespace: String by rootProject.extra
val appCompileSdk: Int by rootProject.extra
val javaVersion: Int by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.sqldelight.coroutines)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.sqldelight.android)
                implementation(libs.sqldelight.jvm)
            }
        }
    }

    jvmToolchain(javaVersion)
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            packageName.set("$appNamespace.database.sqldelight")
        }
    }
}

android {
    namespace = appNamespace
    compileSdk = appCompileSdk
}