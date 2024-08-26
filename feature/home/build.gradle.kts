plugins {
    alias(libs.plugins.bugtracker.android.library)
    alias(libs.plugins.bugtracker.android.library.compose)
    alias(libs.plugins.bugtracker.android.feature)
    alias(libs.plugins.bugtracker.spotless)

    alias(libs.plugins.compose)
}

android {
    namespace = "com.significo.bugtracker.feature.home"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.data)
}
