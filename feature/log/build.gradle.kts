plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:serialization"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:entry"))
                implementation(project(":feature:navigation"))
                implementation(libs.paging)
            }
        }
    }
}