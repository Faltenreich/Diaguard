plugins {
    id("multiplatform-convention")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.components.resources)
            }
        }
    }

    jvmToolchain(Constants.JavaVersion)
}

android {
    namespace = Constants.NameSpace
    compileSdk = Constants.CompileSdk
}