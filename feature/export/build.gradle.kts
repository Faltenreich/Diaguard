plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:config"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:measurement"))
                implementation(project(":feature:preference"))
                implementation(project(":shared"))
            }
        }
    }
}