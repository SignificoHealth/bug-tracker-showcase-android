import com.significo.bugtracker.libs
import dagger.hilt.android.plugin.HiltExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.ksp.get().pluginId)
                apply(libs.plugins.hilt.get().pluginId)
            }

            dependencies {
                "implementation"(libs.hilt.android)
                "ksp"(libs.hilt.compiler)
            }

            extensions.configure<HiltExtension> {
                enableAggregatingTask = true
            }
        }
    }
}
