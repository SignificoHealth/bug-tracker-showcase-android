import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.bugtracker.android.library)
    alias(libs.plugins.bugtracker.android.hilt)
    alias(libs.plugins.bugtracker.spotless)
}

android {
    namespace = "com.significo.bugtracker.core.network"

    defaultConfig {
        val gitHubPat: String? = gradleLocalProperties(rootDir, providers).getProperty("github_pat")
        buildConfigField("String", "GITHUB_PAT", gitHubPat ?: "\"No GitHub PAT found\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.network)
    ksp(libs.moshi.codegen)
}
