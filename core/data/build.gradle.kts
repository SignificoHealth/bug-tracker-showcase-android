plugins {
    alias(libs.plugins.bugtracker.android.library)
    alias(libs.plugins.bugtracker.android.hilt)
    alias(libs.plugins.bugtracker.spotless)
}

android {
    namespace = "com.significo.bugtracker.core.data"
}

dependencies {
    api(projects.core.model)
    api(projects.core.logger)

    implementation(projects.core.network)
    implementation(projects.core.datastore)

    implementation(libs.androidx.paging.runtime)
}
