package com.significo.bugtracker

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
@Suppress("StringLiteralDuplication")
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = platform(libs.androidx.compose.bom)
            "implementation"(bom)
            "androidTestImplementation"(bom)

            "implementation"(libs.bundles.compose.ui)
        }
    }
}
