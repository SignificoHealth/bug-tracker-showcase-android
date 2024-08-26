package com.significo.bugtracker.feature.issue.create.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.significo.bugtracker.components.AppTopBar
import com.significo.bugtracker.core.ui.R
import com.significo.bugtracker.feature.issue.common.ui.DiscardAlertDialog
import com.significo.bugtracker.feature.issue.common.ui.EditorContent

@Composable
fun IssueCreateRoute(
    onBackClick: () -> Unit,
    onIssueUpdated: () -> Unit,
    viewModel: IssueCreateViewModel = hiltViewModel(),
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { viewModel.uploadFile(it) }
        }
    )

    ScreenContent(
        uiState = uiState.value,
        onBackClick = {
            if (uiState.value.hasChanges) {
                showDialog.value = true
            } else {
                onBackClick()
            }
        },
        onIssueChanged = { issue -> viewModel.onIssueChanged(issue) },
        onCreateIssue = {
            viewModel.createIssue(
                onSuccess = {
                    onIssueUpdated()
                    onBackClick()
                }
            )
        },
        onAttachFile = {
            pdfPickerLauncher.launch(
                arrayOf("application/pdf", "image/jpeg", "image/png")
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
    LaunchedEffect(uiState.value.error) {
        uiState.value.error?.let {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: IssueCreateUiState,
    onBackClick: () -> Unit,
    onIssueChanged: (IssueCreate) -> Unit,
    onCreateIssue: () -> Unit,
    onAttachFile: () -> Unit,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.issue_new),
                navigationIcon = R.drawable.ic_back,
                onNavigationClick = onBackClick,
                navigationIconContentDescription = stringResource(id = R.string.accessibility_back),
                actionIcon = R.drawable.ic_attach_file,
                actionIconEnabled = !uiState.isSubmitting,
                onActionClick = onAttachFile,
                actionIconContentDescription = stringResource(id = R.string.accessibility_attach_file),
                action2Icon = R.drawable.ic_save,
                action2IconEnabled = uiState.hasChanges && !uiState.isSubmitting,
                onAction2Click = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onCreateIssue()
                },
                action2IconContentDescription = stringResource(id = R.string.accessibility_save_issue)
            )
        },
        contentWindowInsets = contentWindowInsets
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            EditorContent(
                issue = uiState.issue,
                isSubmitting = uiState.isSubmitting,
                onIssueChanged = onIssueChanged
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewSuccess() {
    AppTheme {
        ScreenContent(
            uiState = IssueCreateUiState(
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
            onIssueChanged = {},
            onCreateIssue = {},
            onAttachFile = {},
            contentWindowInsets = WindowInsets.navigationBars
        )
    }
}
