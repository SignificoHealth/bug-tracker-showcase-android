package com.significo.bugtracker.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.focusable
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.H2Medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes navigationIcon: Int? = null,
    navigationIconContentDescription: String? = null,
    @DrawableRes actionIcon: Int? = null,
    actionIconEnabled: Boolean = true,
    actionIconContentDescription: String? = null,
    @DrawableRes action2Icon: Int? = null,
    action2IconEnabled: Boolean = true,
    action2IconContentDescription: String? = null,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    onAction2Click: () -> Unit = {}
) {
    val view = LocalView.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        view.announceForAccessibility(title)
        focusRequester.requestFocus()
    }

    CenterAlignedTopAppBar(
        title = {
            H2Medium(
                text = title,
                maxLines = 1,
                modifier = Modifier
                    .focusable()
                    .semantics {
                        heading()
                        contentDescription = title
                    }
                    .focusRequester(focusRequester)
            )
        },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        painter = painterResource(id = navigationIcon),
                        contentDescription = navigationIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(
                    enabled = actionIconEnabled,
                    onClick = onActionClick
                ) {
                    Icon(
                        painter = painterResource(id = actionIcon),
                        contentDescription = actionIconContentDescription,
                        tint = if (actionIconEnabled) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            AppTheme.colors.inactiveTint
                        }
                    )
                }
            }
            if (action2Icon != null) {
                IconButton(
                    enabled = action2IconEnabled,
                    onClick = onAction2Click
                ) {
                    Icon(
                        painter = painterResource(id = action2Icon),
                        contentDescription = action2IconContentDescription,
                        tint = if (action2IconEnabled) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            AppTheme.colors.inactiveTint
                        }
                    )
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag("bugtrackerTopAppBar")
    )
}
