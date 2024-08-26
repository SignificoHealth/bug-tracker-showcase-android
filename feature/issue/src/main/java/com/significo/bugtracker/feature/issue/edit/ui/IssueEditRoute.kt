package com.significo.bugtracker.feature.issue.edit.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.components.AppErrorScreen
import com.significo.bugtracker.components.AppProgressLoading
import com.significo.bugtracker.components.AppTopBar
import com.significo.bugtracker.core.ui.R
import com.significo.bugtracker.feature.issue.common.ui.DiscardAlertDialog
import com.significo.bugtracker.feature.issue.common.ui.EditorContent

@Composable
fun IssueEditRoute(
    onBackClick: () -> Unit,
    onIssueUpdated: () -> Unit,
    viewModel: IssueEditViewModel = hiltViewModel(),
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val successData = uiState.value as? IssueEditUiState.Success

    ScreenContent(
        uiState = uiState.value,
        onBackClick = {
            if (successData?.issue?.title?.isBlank() == true || successData?.hasChanges == true) {
                showDialog.value = true
            } else {
                onBackClick()
            }
        },
        onRetry = viewModel::fetchData,
        onIssueChanged = { issue -> viewModel.onIssueChanged(issue) },
        onEditIssue = {
            viewModel.updateIssue(
                onSuccess = {
                    onIssueUpdated()
                    onBackClick()
                }
            )
        },
        contentWindowInsets = contentWindowInsets
    )

    if (showDialog.value) {
        DiscardAlertDialog(
            showDialog = showDialog,
            onDiscardChanges = onBackClick
        )
    }

    val error = stringResource(id = R.string.error_message_unknown)
    LaunchedEffect(successData?.isSubmittingError) {
        if (successData?.isSubmittingError == true) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: IssueEditUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onIssueChanged: (IssueCreate) -> Unit,
    onEditIssue: () -> Unit,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val actionIconEnabled = uiState is IssueEditUiState.Success &&
        uiState.hasChanges &&
        !uiState.isSubmitting

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.issue_edit),
                navigationIcon = R.drawable.ic_back,
                onNavigationClick = onBackClick,
                navigationIconContentDescription = stringResource(id = R.string.accessibility_back),
                actionIcon = R.drawable.ic_save,
                actionIconEnabled = actionIconEnabled,
                onActionClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onEditIssue()
                },
                actionIconContentDescription = stringResource(id = R.string.accessibility_save_issue)
            )
        },
        contentWindowInsets = contentWindowInsets
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            when (uiState) {
                IssueEditUiState.Loading -> AppProgressLoading()
                is IssueEditUiState.Error -> AppErrorScreen(retry = onRetry)
                is IssueEditUiState.Success -> EditorContent(
                    issue = uiState.issue,
                    isSubmitting = uiState.isSubmitting,
                    onIssueChanged = onIssueChanged
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewLoading() {
    AppTheme {
        ScreenContent(
            uiState = IssueEditUiState.Loading,
            onBackClick = {},
            onRetry = {},
            onIssueChanged = {},
            onEditIssue = {},
            contentWindowInsets = WindowInsets.navigationBars
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewError() {
    AppTheme {
        ScreenContent(
            uiState = IssueEditUiState.Error(RuntimeException("Error")),
            onBackClick = {},
            onRetry = {},
            onIssueChanged = {},
            onEditIssue = {},
            contentWindowInsets = WindowInsets.navigationBars
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewSuccess() {
    AppTheme {
        ScreenContent(
            uiState = IssueEditUiState.Success(
                issue = IssueCreate(
                    title = "Issue title",
                    body = """
                        # Welcome to the Markdown Editor

                        This is a simple **Markdown editor** built using Jetpack Compose and CommonMark.

                        ## Features

                        - **Bold** text
                        - *Italic* text
                        - [Links](https://www.example.com)
                        - Inline `code`
                        - Code blocks:
                            ```kotlin
                            val greeting = "Hello, World!"
                            println(greeting)
                            ```
                        - Blockquotes:
                            > This is a blockquote.
                        - Lists:
                            1. First item
                            2. Second item
                            3. Third item
                        - Another list:
                          - subitem

                        ## More Examples

                        ### Headings

                        # Heading 1
                        ## Heading 2
                        ### Heading 3
                        #### Heading 4
                        ##### Heading 5
                        ###### Heading 6

                        ### Images

                        ![Alt text](https://via.placeholder.com/150)

                        ### Horizontal Rule

                        ---

                        ### Tables

                        | Syntax | Description |
                        | ----------- | ----------- |
                        | Header | Title |
                        | Paragraph | Text |

                        ### Task List

                        - [x] Task 1
                        - [ ] Task 2
                        - [ ] Task 3

                        ### Strikethrough

                        ~~This was mistaken text~~
                    """.trimIndent()
                )
            ),
            onBackClick = {},
            onRetry = {},
            onIssueChanged = {},
            onEditIssue = {},
            contentWindowInsets = WindowInsets.navigationBars
        )
    }
}
