package com.significo.bugtracker

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureJavaAndKotlinVersions() {
    extensions.getByType<JavaPluginExtension>().apply {
        this.sourceCompatibility = JavaVersion.VERSION_17
        this.targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
}

fun Project.registerTestTasks() {
    tasks.register("debugTest") {
        dependsOn("test")
    }
    tasks.register("stagingTest") {
        dependsOn("test")
    }
    tasks.register("prodTest") {
        dependsOn("test")
    }
}
