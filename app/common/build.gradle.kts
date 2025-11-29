plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":shared"))
                implementation(project(":feature:dashboard"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
                implementation(project(":feature:statistic"))
                api(project(":data"))
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
            }
        }
    }
}