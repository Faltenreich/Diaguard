plugins {
    id("multiplatform-convention")
    alias(libs.plugins.sqldelight)
}

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

    jvmToolchain(Constants.JavaVersion)
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            packageName.set("${Constants.NameSpace}.database.sqldelight")
        }
    }
}

android {
    namespace = Constants.NameSpace
    compileSdk = Constants.CompileSdk
}