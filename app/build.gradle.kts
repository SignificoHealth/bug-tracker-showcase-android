plugins {
    alias(libs.plugins.bugtracker.android.application)
    alias(libs.plugins.bugtracker.android.application.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.bugtracker.spotless)
}

android {
    namespace = "com.significo.bugtracker"

    defaultConfig {
        applicationId = "com.significo.bugtracker"

        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            matchingFallbacks.add("release")
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "password"
            keyAlias = "debug"
            keyPassword = "password"
        }
    }

    lint {
        xmlReport = false
        lintConfig = file("$rootDir/config/lint/lint.xml")
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources.excludes.add("LICENSE.txt")
        resources.excludes.add("LICENSE.txt")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/INDEX.LIST")
        resources.excludes.add("META-INF/services/javax.annotation.processing.Processor")
        resources.excludes.add("META-INF/io.netty.versions.properties")
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.data)

    implementation(projects.feature.home)
    implementation(projects.feature.about)
    implementation(projects.feature.issues)
    implementation(projects.feature.issue)

    implementation(libs.bundles.network)
    debugImplementation(libs.bundles.debug)
}
