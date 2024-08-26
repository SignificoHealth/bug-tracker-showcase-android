package com.significo.bugtracker.navigation

import com.significo.bugtracker.AppIcons
import com.significo.bugtracker.Icon
import com.significo.bugtracker.core.ui.R

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int
) {
    HOME(
        selectedIcon = Icon.DrawableResourceIcon(AppIcons.Home),
        unselectedIcon = Icon.DrawableResourceIcon(AppIcons.Home),
        iconTextId = R.string.tab_home
    ),
    ABOUT(
        selectedIcon = Icon.DrawableResourceIcon(AppIcons.About),
        unselectedIcon = Icon.DrawableResourceIcon(AppIcons.About),
        iconTextId = R.string.tab_about
    )
}
