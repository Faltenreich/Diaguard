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
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.viewmodel)
                implementation(project(":core:injection"))
            }
            all {
                languageSettings {
                    optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
            }
        }
    }
}