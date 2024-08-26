plugins {
    alias(libs.plugins.bugtracker.android.library)
    alias(libs.plugins.bugtracker.android.library.compose)
    alias(libs.plugins.bugtracker.android.feature)
    alias(libs.plugins.bugtracker.spotless)

    alias(libs.plugins.compose)
}

android {
    namespace = "com.significo.bugtracker.feature.issues"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
}
