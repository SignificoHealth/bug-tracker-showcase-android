plugins {
    alias(libs.plugins.bugtracker.android.library)
    alias(libs.plugins.bugtracker.android.hilt)
    alias(libs.plugins.bugtracker.spotless)
}

android {
    namespace = "com.significo.bugtracker.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}
