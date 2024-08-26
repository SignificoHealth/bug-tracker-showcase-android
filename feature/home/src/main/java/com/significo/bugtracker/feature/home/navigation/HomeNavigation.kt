package com.significo.bugtracker.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.significo.bugtracker.feature.home.ui.HomeRoute

const val HOME_NAVIGATION_ROUTE = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(navigateToIssues: () -> Unit) {
    composable(route = HOME_NAVIGATION_ROUTE) {
        HomeRoute(
            navigateToIssues = navigateToIssues
        )
    }
}
