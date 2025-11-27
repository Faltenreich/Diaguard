plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:architecture"))
                implementation(project(":core:config"))
                implementation(project(":core:datetime"))
                implementation(project(":core:injection"))
                implementation(project(":core:localization"))
                implementation(project(":core:logging"))
                implementation(project(":core:network"))
                implementation(project(":core:persistence"))
                implementation(project(":core:serialization"))
                implementation(project(":core:system"))
                implementation(project(":core:view"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
                api(project(":data"))
                api(project(":shared"))
            }
        }
    }
}