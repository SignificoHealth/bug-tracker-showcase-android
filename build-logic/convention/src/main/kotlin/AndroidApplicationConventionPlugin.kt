import com.android.build.api.dsl.ApplicationExtension
import com.significo.bugtracker.configureAndroidCompose
import com.significo.bugtracker.configureJavaAndKotlinVersions
import com.significo.bugtracker.configureKotlinAndroid
import com.significo.bugtracker.libs
import com.significo.bugtracker.registerTestTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.application.get().pluginId)
                apply(libs.plugins.kotlin.android.get().pluginId)
                apply(libs.plugins.bugtracker.android.hilt.get().pluginId)
            }

            configureJavaAndKotlinVersions()
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                defaultConfig.targetSdk = libs.versions.targetSdk.get().toInt()
            }

            registerTestTasks()

            dependencies {
                "implementation"(libs.bundles.test)
            }
        }
    }
}
