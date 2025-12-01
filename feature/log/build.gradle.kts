plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:serialization"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:navigation"))
                implementation(project(":shared"))
                implementation(libs.paging)
            }
        }
    }
}