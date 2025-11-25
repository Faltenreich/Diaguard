plugins {
    id("feature-convention")
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:config"))
                implementation(project(":core:persistence"))
                implementation(project(":core:system"))
                implementation(project(":feature:navigation"))
                implementation(libs.aboutlibraries.core)
                implementation(libs.aboutlibraries.compose)
            }
        }
    }
}