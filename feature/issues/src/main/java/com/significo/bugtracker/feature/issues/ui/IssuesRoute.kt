package com.significo.bugtracker.feature.issues.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.H3Medium
import com.significo.bugtracker.Issue
import com.significo.bugtracker.State
import com.significo.bugtracker.TextLight
import com.significo.bugtracker.components.AppScreenStateAwarePaginatedList
import com.significo.bugtracker.components.AppTopBar
import com.significo.bugtracker.components.EmptyState
import com.significo.bugtracker.core.ui.R
import com.significo.bugtracker.extensions.isScrollingUp
import com.significo.bugtracker.extensions.toPrettyTime
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

@Composable
fun rememberIssuesState() = remember { IssuesState() }

@Stable
class IssuesState {
    var shouldRefresh by mutableStateOf(false)
}

@Composable
fun IssuesRoute(
    state: IssuesState,
    viewModel: IssuesViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    navigateToIssue: (issueNumber: Int) -> Unit,
    navigateToIssueCreate: () -> Unit,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

    if (state.shouldRefresh) {
        LaunchedEffect(Unit) {
            state.shouldRefresh = false
            viewModel.refresh()
        }
    }

    IssuesScreen(
        items = pagingData,
        onBackClick = onBackClick,
        onRetry = { viewModel.refresh() },
        onItemClicked = { navigateToIssue(it) },
        navigateToIssueCreate = navigateToIssueCreate,
        contentWindowInsets = contentWindowInsets
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssuesScreen(
    items: LazyPagingItems<Issue>,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onItemClicked: (Int) -> Unit,
    navigateToIssueCreate: () -> Unit,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.issues),
                navigationIcon = R.drawable.ic_back,
                onNavigationClick = onBackClick,
                navigationIconContentDescription = stringResource(id = R.string.accessibility_back)
            )
        },
        contentWindowInsets = contentWindowInsets
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            val scrollState = rememberLazyListState()
            AppScreenStateAwarePaginatedList(
                items = items,
                retry = onRetry,
                scrollState = scrollState,
                emptyState = EmptyState(
                    titleRes = R.string.empty_content_title_default,
                    description = stringResource(R.string.empty_content_body_default),
                    drawableRes = R.drawable.ic_check_circle
                )
            ) {
                items(
                    count = items.itemCount,
                    key = { index -> items[index]?.number ?: UUID.randomUUID() }
                ) { index ->
                    IssueItemContent(
                        item = items[index]!!,
                        onItemClicked = onItemClicked
                    )
                    if (index < items.itemCount - 1) {
                        HorizontalDivider()
                    }
                }
                item {
                    Spacer(Modifier.height(Dimensions.Medium))
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Dimensions.Small),
                visible = scrollState.isScrollingUp(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = navigateToIssueCreate
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.accessibility_add_new_issue),
                        tint = AppTheme.colors.secondaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun IssueItemContent(
    item: Issue,
    onItemClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item.number) }
            .padding(horizontal = Dimensions.Small, vertical = Dimensions.ExtraSmall)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = when (item.state) {
                        State.OPEN -> R.drawable.ic_issue_opened
                        State.CLOSED -> R.drawable.ic_issue_closed
                    }
                ),
                contentDescription = stringResource(
                    id = when (item.state) {
                        State.OPEN -> R.string.accessibility_issue_opened
                        State.CLOSED -> R.string.accessibility_issue_closed
                    }
                ),
                tint = when (item.state) {
                    State.OPEN -> AppTheme.colors.success
                    State.CLOSED -> AppTheme.colors.purple
                },
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(Dimensions.ExtraSmall))
            H3Medium(text = item.title)
        }
        Row {
            TextLight(text = "#${item.number}")
            Spacer(Modifier.width(Dimensions.ExtraSmall))
            TextLight(text = item.createdAt.toPrettyTime())
            item.user?.login?.let {
                Spacer(Modifier.width(Dimensions.ExtraSmall))
                TextLight(text = stringResource(id = R.string.by_author, it))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewItem() {
    AppTheme {
        IssueItemContent(
            item = Issue(
                number = 1,
                state = State.CLOSED,
                title = "Issue title",
                body = "Issue body",
                user = null,
                labels = listOf(),
                assignee = null,
                assignees = listOf(),
                comments = 8430,
                closedAt = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            onItemClicked = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewList() {
    AppTheme {
        IssuesScreen(
            items = flowOf(
                PagingData.from(
                    List(3) {
                        Issue(
                            number = 1,
                            state = State.OPEN,
                            title = "Issue title",
                            body = "Issue body",
                            user = null,
                            labels = listOf(),
                            assignee = null,
                            assignees = listOf(),
                            comments = 0,
                            closedAt = null,
                            createdAt = "2024-01-01T00:00:00Z",
                            updatedAt = "2024-01-01T00:00:00Z"
                        )
                    }
                )
            ).collectAsLazyPagingItems(),
            onBackClick = {},
            onRetry = {},
            onItemClicked = {},
            navigateToIssueCreate = {},
            contentWindowInsets = WindowInsets.navigationBars
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewEmptyList() {
    AppTheme {
        IssuesScreen(
            items = flowOf(
                PagingData.from(emptyList<Issue>())
            ).collectAsLazyPagingItems(),
            onBackClick = {},
            onRetry = {},
            onItemClicked = {},
            navigateToIssueCreate = {},
            contentWindowInsets = WindowInsets.navigationBars
        )
    }
}
