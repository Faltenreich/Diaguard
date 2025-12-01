plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:system"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:measurement"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
                implementation(libs.paging)
            }
        }
    }
}