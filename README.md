![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)

![Lint Status](https://github.com/SignificoHealth/bug-tracker-showcase-android/actions/workflows/lint.yml/badge.svg)
![Tests Status](https://github.com/SignificoHealth/bug-tracker-showcase-android/actions/workflows/test.yml/badge.svg)

## Project Overview

This project serves as a comprehensive Android showcase, demonstrating the implementation of key modern technologies and architectural patterns. Entirely written in [Kotlin](https://kotlinlang.org/), it follows the [guide to app architecture](https://developer.android.com/jetpack/guide) to ensure best practices in Android development.

Key technologies and tools used in this project include:

- **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: Used for building declarative UIs, enabling more concise and intuitive UI development.
- **[Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)**: Employed throughout the project to handle asynchronous programming, providing a more efficient and readable approach to managing background tasks.
- **[Retrofit](https://square.github.io/retrofit/)**: A type-safe HTTP client for Android and Java, used for managing network requests and parsing responses.
- **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)**: A dependency injection library for Android, simplifying the process of managing dependencies and improving code modularity.
- **[Gradle Multi-Module Setup](https://developer.android.com/topic/modularization)**: Organizes the project into multiple Gradle modules to enhance modularity, scalability, and maintainability.


## Requirements

To set up the project, ensure you meet the following requirements:

- **Development Environment**: Use at least [Android Studio Koala](https://developer.android.com/studio) for development.
- **Java Version**: Set the `JAVA_HOME` environment variable to point to JDK version 17.
- **Android Compatibility**: The application is compatible with devices running Android 8.0 Oreo (API level 26) and above.
- **GitHub Personal Access Token (PAT)**: To run the project, add the following key in the `local.properties` file:
    ```
    github_pat="{pat}"
    ```

This `github_pat` refers to a personal GitHub access token with permissions to create and update issues. The required token will be provided upon request by Significo. Please contact us at dev@significo.com to obtain the necessary token.

For more information on GitHub PATs, refer to the [GitHub documentation on granular PATs](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).


## Showcase App

This application provides a seamless way to manage your GitHub issues, leveraging the [GitHub API](https://docs.github.com/en/rest/repos/contents?apiVersion=2022-11-28).

### Technical Features:

- **View Issues**: Demonstrates how to fetch data using GET operations from the GitHub API. This feature retrieves and displays all open issues in one centralized location, showcasing effective data fetching and UI integration.
- **Create Issues**: Illustrates the use of POST and PUT operations to create new issues on GitHub. This includes providing all necessary information and the ability to attach files, such as PDFs and images, highlighting file upload capabilities within API interactions.
- **Edit Issues**: Showcases the use of PATCH operations to update existing issues. This feature ensures that issue data can be modified and kept current, demonstrating efficient data update mechanisms and synchronization with GitHub.

By working directly with GitHub, this app ensures that all changes are reflected immediately, maintaining real-time synchronization with your repositories.

### Accessibility

This application is accessible, supporting TalkBack and featuring user-friendly UI components, content descriptions, and compliance with [accessibility guidelines](https://developer.android.com/develop/ui/compose/accessibility) to ensure a seamless experience for all users.

## Code Analysis Tools

To ensure high code quality and maintainability, we have integrated two essential code analysis tools into the project: Spotless and Detekt. These tools help enforce coding standards, detect potential issues early, and maintain a consistent codebase.

### Spotless
[Spotless](https://github.com/diffplug/spotless) is a code formatting tool that automatically ensures your code adheres to specified style guidelines.

Commands:
- **Check for formatting issues** without applying changes:
    ```bash
    ./gradlew spotlessCheck
    ```
    This command scans the project and reports any formatting violations based on the Spotless configuration.

- **Automatically format your code** according to the defined rules:
    ```bash
    ./gradlew spotlessApply
    ```
    This command reformats your source files to adhere to the style guidelines specified in the Spotless configuration.

### Detekt
[Detekt](https://detekt.github.io/detekt/) is a static code analysis tool specifically designed for Kotlin.

Commands:
- **Run all checks**:
    ```bash
    ./gradlew detektAll
    ```

- **Ignore violations in the baseline**:
    ```bash
    ./gradlew detektAllBaseline
    ```

The baseline is located at `config/detekt/baseline.xml`. However, using `@Suppress("")` in the code directly is preferable so that ignored violations are visible when browsing the code.
