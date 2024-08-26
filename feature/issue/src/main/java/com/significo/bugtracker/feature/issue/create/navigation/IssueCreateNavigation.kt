package com.significo.bugtracker.feature.issue.create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.significo.bugtracker.feature.issue.create.ui.IssueCreateRoute

private const val ISSUE_CREATE_NAVIGATION_ROUTE = "issue_create"

fun NavController.navigateToIssueCreate(navOptions: NavOptions? = null) {
    this.navigate(ISSUE_CREATE_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.issueCreateScreen(
    onBackClick: () -> Unit,
    onIssueUpdated: () -> Unit
) {
    composable(route = ISSUE_CREATE_NAVIGATION_ROUTE) {
        IssueCreateRoute(
            onBackClick = onBackClick,
            onIssueUpdated = onIssueUpdated
        )
    }
}
