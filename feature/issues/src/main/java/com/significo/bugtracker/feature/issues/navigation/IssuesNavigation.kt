package com.significo.bugtracker.feature.issues.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.significo.bugtracker.feature.issues.ui.IssuesRoute
import com.significo.bugtracker.feature.issues.ui.IssuesState

const val ISSUES_NAVIGATION_ROUTE = "issues"

fun NavController.navigateToIssues(navOptions: NavOptions? = null) {
    this.navigate(ISSUES_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.issuesScreen(
    state: IssuesState,
    onBackClick: () -> Unit,
    navigateToIssue: (issueNumber: Int) -> Unit,
    navigateToIssueCreate: () -> Unit
) {
    composable(route = ISSUES_NAVIGATION_ROUTE) {
        IssuesRoute(
            state = state,
            onBackClick = onBackClick,
            navigateToIssue = navigateToIssue,
            navigateToIssueCreate = navigateToIssueCreate
        )
    }
}
