package com.significo.bugtracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.significo.bugtracker.feature.about.navigation.ABOUT_NAVIGATION_ROUTE
import com.significo.bugtracker.feature.about.navigation.navigateToAbout
import com.significo.bugtracker.feature.home.navigation.HOME_NAVIGATION_ROUTE
import com.significo.bugtracker.feature.home.navigation.navigateToHome
import com.significo.bugtracker.feature.issue.detail.ui.IssueState
import com.significo.bugtracker.feature.issue.detail.ui.rememberIssueState
import com.significo.bugtracker.feature.issues.ui.IssuesState
import com.significo.bugtracker.feature.issues.ui.rememberIssuesState
import com.significo.bugtracker.navigation.TopLevelDestination

@Composable
fun rememberBugtrackerAppState(
    navController: NavHostController = rememberNavController(),
    issueState: IssueState = rememberIssueState(),
    issuesState: IssuesState = rememberIssuesState()
): BugTrackerAppState = remember(navController) {
    BugTrackerAppState(
        navController = navController,
        issueState = issueState,
        issuesState = issuesState
    )
}

@Stable
class BugTrackerAppState(
    val navController: NavHostController,
    val issueState: IssueState,
    val issuesState: IssuesState
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    private val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            HOME_NAVIGATION_ROUTE -> TopLevelDestination.HOME
            ABOUT_NAVIGATION_ROUTE -> TopLevelDestination.ABOUT
            else -> null
        }

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.ABOUT -> navController.navigateToAbout(topLevelNavOptions)
        }
    }

    val shouldShowBottomBar: Boolean
        @Composable get() = currentTopLevelDestination != null

    fun onBackClick() {
        navController.popBackStack()
    }

    fun onIssueUpdated() {
        issueState.shouldRefresh = true
        issuesState.shouldRefresh = true
    }
}
