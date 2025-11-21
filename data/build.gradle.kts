plugins {
    id("multiplatform-convention")
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
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}