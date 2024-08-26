plugins {
    alias(libs.plugins.bugtracker.android.library)
    alias(libs.plugins.bugtracker.android.library.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.bugtracker.spotless)
}

android {
    namespace = "com.significo.bugtracker.core.ui"
}

dependencies {
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.accompanist.swiperefresh)
    implementation(libs.prettyTime)
    implementation(libs.compose.richtext.commonmark)
    implementation(libs.compose.richtext.ui.material)
}
