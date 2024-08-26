package com.significo.bugtracker.feature.issue.edit.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.significo.bugtracker.feature.issue.edit.ui.IssueEditRoute

internal const val IdArg = "id"
private const val ISSUE_EDIT_NAVIGATION_ROUTE = "issue_edit/{$IdArg}"

internal open class IssueEditArgs(open val id: Int) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        id = checkNotNull(savedStateHandle[IdArg])
    )
}

fun NavController.navigateToIssueEdit(
    issueNumber: Int,
    navOptions: NavOptions? = null
) {
    val route = ISSUE_EDIT_NAVIGATION_ROUTE.replace(
        oldValue = "{$IdArg}",
        newValue = NavType.IntType.serializeAsValue(issueNumber)
    )
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.issueEditScreen(
    onBackClick: () -> Unit,
    onIssueUpdated: () -> Unit
) {
    composable(
        route = ISSUE_EDIT_NAVIGATION_ROUTE,
        arguments = listOf(
            navArgument(IdArg) { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val itemId = backStackEntry.arguments?.getInt(IdArg)
        checkNotNull(itemId)
        IssueEditRoute(
            onBackClick = onBackClick,
            onIssueUpdated = onIssueUpdated
        )
    }
}
