import com.android.build.api.dsl.LibraryExtension
import com.significo.bugtracker.configureKotlinAndroid
import com.significo.bugtracker.libs
import com.significo.bugtracker.registerTestTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.library.get().pluginId)
                apply(libs.plugins.kotlin.android.get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.versions.targetSdk.get().toInt()
                testOptions.targetSdk = libs.versions.targetSdk.get().toInt()
                testOptions.unitTests.isReturnDefaultValues = true
                lint.targetSdk = libs.versions.targetSdk.get().toInt()
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                defaultConfig.vectorDrawables.useSupportLibrary = true
            }

            dependencies {
                "implementation"(libs.bundles.ui)
            }

            registerTestTasks()
        }
    }
}
