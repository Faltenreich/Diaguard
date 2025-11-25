plugins {
    id("multiplatform-convention")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datetime"))
                implementation(project(":core:injection"))
                implementation(project(":core:localization"))
                implementation(project(":core:logging"))
                implementation(project(":core:network"))
                implementation(project(":core:persistence"))
                implementation(project(":core:serialization"))
                implementation(project(":core:view"))
                implementation(compose.components.resources)
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

sqldelight {
    databases {
        create("SqlDelightApi") {
            val appNamespace: String by rootProject.extra
            packageName.set("$appNamespace.data")
        }
    }
}