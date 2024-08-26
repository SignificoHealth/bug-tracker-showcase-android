package com.significo.bugtracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.significo.bugtracker.feature.about.navigation.aboutScreen
import com.significo.bugtracker.feature.home.navigation.HOME_NAVIGATION_ROUTE
import com.significo.bugtracker.feature.home.navigation.homeScreen
import com.significo.bugtracker.feature.issue.create.navigation.issueCreateScreen
import com.significo.bugtracker.feature.issue.create.navigation.navigateToIssueCreate
import com.significo.bugtracker.feature.issue.detail.navigation.issueDetailScreen
import com.significo.bugtracker.feature.issue.detail.navigation.navigateToIssueDetail
import com.significo.bugtracker.feature.issue.detail.ui.IssueState
import com.significo.bugtracker.feature.issue.edit.navigation.issueEditScreen
import com.significo.bugtracker.feature.issue.edit.navigation.navigateToIssueEdit
import com.significo.bugtracker.feature.issues.navigation.issuesScreen
import com.significo.bugtracker.feature.issues.navigation.navigateToIssues
import com.significo.bugtracker.feature.issues.ui.IssuesState

@Composable
fun BugtrackerNavHost(
    navController: NavHostController,
    onBackClick: () -> Unit,
    onIssueUpdated: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_NAVIGATION_ROUTE,
    issueState: IssueState,
    issuesState: IssuesState
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(navigateToIssues = navController::navigateToIssues)
        aboutScreen()
        issuesScreen(
            state = issuesState,
            onBackClick = onBackClick,
            navigateToIssue = { navController.navigateToIssueDetail(it) },
            navigateToIssueCreate = { navController.navigateToIssueCreate() }
        )
        issueDetailScreen(
            state = issueState,
            onBackClick = onBackClick,
            navigateToIssueEdit = { navController.navigateToIssueEdit(it) }
        )
        issueCreateScreen(
            onBackClick = onBackClick,
            onIssueUpdated = onIssueUpdated
        )
        issueEditScreen(
            onBackClick = onBackClick,
            onIssueUpdated = onIssueUpdated
        )
    }
}
