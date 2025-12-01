plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":shared"))
                implementation(project(":feature:backup"))
                implementation(project(":feature:dashboard"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:export"))
                implementation(project(":feature:log"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
                implementation(project(":feature:startup"))
                implementation(project(":feature:statistic"))
                implementation(project(":feature:tag"))
                implementation(project(":feature:timeline"))
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