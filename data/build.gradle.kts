plugins {
    id("multiplatform-convention")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datetime"))
                implementation(project(":core:localization"))
                implementation(project(":core:logging"))
                implementation(project(":core:network"))
                implementation(project(":core:persistence"))
                implementation(project(":core:serialization"))
                implementation(project(":core:view"))
                implementation(compose.components.uiToolingPreview)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.sqldelight.coroutines)
            }
        }
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }
    }
}