import com.android.build.gradle.LibraryExtension
import com.significo.bugtracker.configureKotlinAndroid
import com.significo.bugtracker.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.library.get().pluginId)
                apply(libs.plugins.kotlin.android.get().pluginId)
                apply(libs.plugins.bugtracker.android.hilt.get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.versions.targetSdk.get().toInt()
            }

            dependencies {
                "implementation"(libs.androidx.compose.activity)
                "implementation"(libs.androidx.hilt.navigation.compose)
                "implementation"(libs.bundles.ui)

                add("testImplementation", kotlin("test"))

                "testImplementation"(libs.bundles.test)
                add("androidTestImplementation", kotlin("test"))
            }
        }
    }
}
