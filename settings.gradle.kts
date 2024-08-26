pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Enable type-safe project accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "BugTracker"

include(
    ":app",
    ":core:logger",
    ":core:data",
    ":core:datastore",
    ":core:model",
    ":core:network",
    ":core:ui",
    ":feature:about",
    ":feature:home",
    ":feature:issues",
    ":feature:issue",
    ":lint-rules"
)
