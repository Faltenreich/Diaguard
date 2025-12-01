plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:datetime"))
                implementation(project(":feature:entry"))
                implementation(project(":feature:measurement"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
            }
        }
    }
}