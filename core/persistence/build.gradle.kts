plugins {
    id("multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:config"))
                implementation(project(":core:injection"))
                implementation(project(":core:localization"))
                implementation(project(":core:logging"))
                implementation(libs.datastore)
                implementation(libs.kotlinx.io)
                implementation(libs.sqldelight.coroutines)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.android.preferences)
                implementation(libs.sqldelight.android)
                implementation(libs.sqldelight.jvm)
            }
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.junit)
                implementation(libs.android.test.runner)
                implementation(libs.android.test.junit)
                implementation(libs.android.test.junit.ktx)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }
    }
}