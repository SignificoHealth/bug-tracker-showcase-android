package com.significo.bugtracker.feature.issue.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.significo.bugtracker.AppRichText
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.H2Medium
import com.significo.bugtracker.Issue
import com.significo.bugtracker.LabelLight
import com.significo.bugtracker.State
import com.significo.bugtracker.TextLight
import com.significo.bugtracker.User
import com.significo.bugtracker.components.AppErrorScreen
import com.significo.bugtracker.components.AppProgressLoading
import com.significo.bugtracker.components.AppTopBar
import com.significo.bugtracker.components.annotatedStringResource
import com.significo.bugtracker.core.ui.R
import com.significo.bugtracker.extensions.toPrettyTime

@Composable
fun rememberIssueState() = remember { IssueState() }

@Stable
class IssueState {
    var shouldRefresh by mutableStateOf(false)
}

@Composable
fun IssueDetailRoute(
    state: IssueState,
    onBackClick: () -> Unit,
    navigateToIssueEdit: (issueNumber: Int) -> Unit,
    viewModel: IssueDetailViewModel = hiltViewModel(),
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    val uiState = viewModel.uiState.collectAsState()

    if (state.shouldRefresh) {
        LaunchedEffect(Unit) {
            state.shouldRefresh = false
            viewModel.fetchData()
        }
    }

    ScreenContent(
        uiState = uiState.value,
        onBackClick = onBackClick,
        onRetry = viewModel::fetchData,
        navigateToIssueEdit = navigateToIssueEdit,
        contentWindowInsets = contentWindowInsets
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: IssueDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    navigateToIssueEdit: (issueNumber: Int) -> Unit,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    val issue = (uiState as? IssueDetailUiState.Success)?.issue

    Scaffold(
        topBar = {
            AppTopBar(
                title = issue?.title.orEmpty(),
                navigationIcon = R.drawable.ic_back,
                onNavigationClick = onBackClick,
                navigationIconContentDescription = stringResource(id = R.string.accessibility_back),
                actionIcon = R.drawable.ic_edit.takeIf { issue != null },
                actionIconEnabled = issue?.number?.let { it != 0 } ?: false,
                onActionClick = {
                    val issueNumber = issue?.number
                    checkNotNull(issueNumber)
                    navigateToIssueEdit(issueNumber)
                },
                actionIconContentDescription = stringResource(id = R.string.accessibility_edit_issue)
            )
        },
        contentWindowInsets = contentWindowInsets
    ) { contentPadding ->
        Box(
            modifier = Modifier.padding(contentPadding)
        ) {
            when (uiState) {
                IssueDetailUiState.Loading -> AppProgressLoading()
                is IssueDetailUiState.Success -> IssueContent(issue = uiState.issue)
                is IssueDetailUiState.Error -> AppErrorScreen(retry = onRetry)
            }
        }
    }
}

@Composable
private fun IssueContent(issue: Issue) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.Small, horizontal = Dimensions.Medium)
            .verticalScroll(rememberScrollState())
    ) {
        StateLabel(issue.state)
        Spacer(Modifier.height(Dimensions.Tiny))
        H2Medium(
            text = annotatedStringResource(
                R.string.issue_title_number,
                issue.title,
                issue.number
            )
        )
        Spacer(Modifier.height(Dimensions.Tiny))
        Row {
            TextLight(
                text = annotatedStringResource(
                    R.string.issue_opened_by,
                    issue.user?.login ?: "--",
                    issue.createdAt.toPrettyTime()
                )
            )
        }
        Spacer(Modifier.height(Dimensions.Small))
        AppRichText(text = issue.body.orEmpty())
    }
}

@Composable
private fun StateLabel(state: State) {
    val contentDescriptionIssueOpened = stringResource(id = R.string.accessibility_issue_opened)

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimensions.Tiny),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = when (state) {
                    State.OPEN -> AppTheme.colors.success.copy(alpha = .1f)
                    State.CLOSED -> AppTheme.colors.purple.copy(alpha = .1f)
                },
                shape = RoundedCornerShape(size = 12.dp)
            )
            .padding(horizontal = Dimensions.ExtraSmall, vertical = Dimensions.Tiny)
            .semantics(mergeDescendants = true) { contentDescription = contentDescriptionIssueOpened }
    ) {
        Icon(
            painter = painterResource(
                id = when (state) {
                    State.OPEN -> R.drawable.ic_issue_opened
                    State.CLOSED -> R.drawable.ic_issue_closed
                }
            ),
            contentDescription = null,
            tint = when (state) {
                State.OPEN -> AppTheme.colors.success
                State.CLOSED -> AppTheme.colors.purple
            },
            modifier = Modifier.size(16.dp)
        )

        LabelLight(
            text = stringResource(
                id = when (state) {
                    State.OPEN -> R.string.issue_opened
                    State.CLOSED -> R.string.issue_closed
                }
            ),
            color = when (state) {
                State.OPEN -> AppTheme.colors.success
                State.CLOSED -> AppTheme.colors.purple
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun Preview() {
    AppTheme {
        IssueContent(
            issue = Issue(
                number = 1,
                state = State.OPEN,
                title = "Issue title",
                body = "Issue body",
                user = User(
                    id = 6711,
                    name = "User",
                    email = null,
                    login = "username",
                    avatarUrl = ""
                ),
                labels = listOf(),
                assignee = null,
                assignees = listOf(),
                comments = 8430,
                closedAt = null,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            )
        )
    }
}
