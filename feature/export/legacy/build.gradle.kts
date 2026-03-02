plugins {
    id("com.android.library")
}

android {
    compileSdk = 36
    namespace = "com.faltenreich.diaguard"

    defaultConfig {
        minSdk = 21
    }
}

dependencies {
    implementation(project(":feature:export:pdfjet"))
    implementation("net.danlew:android.joda:2.12.5")
    implementation("com.opencsv:opencsv:5.8")
    implementation("org.apache.commons:commons-text:1.10.0")
}