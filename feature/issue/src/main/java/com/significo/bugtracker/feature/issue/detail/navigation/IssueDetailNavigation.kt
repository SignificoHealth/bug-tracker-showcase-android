package com.significo.bugtracker.feature.issue.detail.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.significo.bugtracker.feature.issue.detail.ui.IssueDetailRoute
import com.significo.bugtracker.feature.issue.detail.ui.IssueState

internal const val IdArg = "id"
private const val ISSUE_DETAIL_NAVIGATION_ROUTE = "issue/{$IdArg}"

internal open class IssueArgs(open val id: Int) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        id = checkNotNull(savedStateHandle[IdArg])
    )
}

fun NavController.navigateToIssueDetail(
    issueNumber: Int,
    navOptions: NavOptions? = null
) {
    val route = ISSUE_DETAIL_NAVIGATION_ROUTE.replace(
        oldValue = "{$IdArg}",
        newValue = NavType.IntType.serializeAsValue(issueNumber)
    )
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.issueDetailScreen(
    state: IssueState,
    onBackClick: () -> Unit,
    navigateToIssueEdit: (issueNumber: Int) -> Unit
) {
    composable(
        route = ISSUE_DETAIL_NAVIGATION_ROUTE,
        arguments = listOf(
            navArgument(IdArg) { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val itemId = backStackEntry.arguments?.getInt(IdArg)
        checkNotNull(itemId)
        IssueDetailRoute(
            state = state,
            onBackClick = onBackClick,
            navigateToIssueEdit = { navigateToIssueEdit(itemId) }
        )
    }
}
