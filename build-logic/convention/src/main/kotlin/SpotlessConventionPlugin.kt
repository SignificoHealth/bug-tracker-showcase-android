import com.diffplug.gradle.spotless.SpotlessExtension
import com.significo.bugtracker.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugins.spotless.get().pluginId)

            extensions.configure<SpotlessExtension> {
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt")
                    ktlint()
                        .setEditorConfigPath("$rootDir/.editorconfig")
                        .editorConfigOverride(
                            mapOf(
                                "max_line_length" to 140,
                                "ij_kotlin_allow_trailing_comma" to false,
                                "ij_kotlin_allow_trailing_comma_on_call_site" to false,
                                "ij_kotlin_packages_to_use_import_on_demand" to false,
                                "ktlint_experimental" to "enabled",
                                "ktlint_class_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to 2,
                                "ktlint_function_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to 2,
                                "ktlint_standard_condition-wrapping" to "disabled",
                                "ktlint_standard_function-naming" to "disabled",
                                "ktlint_standard_property-naming" to "disabled"
                            )
                        )
                }
            }
        }
    }
}
