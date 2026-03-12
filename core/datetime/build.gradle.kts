plugins {
    id("multiplatform-convention")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:config"))
                implementation(project(":core:localization"))
                implementation(project(":core:serialization"))
                implementation(libs.compose.resources)
                implementation(libs.compose.foundation)
                implementation(libs.kotlinx.dateTime)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":core:test"))
            }
        }
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }
    }
}