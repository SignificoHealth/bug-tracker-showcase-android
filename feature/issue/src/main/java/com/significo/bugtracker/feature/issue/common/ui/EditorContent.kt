package com.significo.bugtracker.feature.issue.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.significo.bugtracker.AppRichText
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.H1Medium
import com.significo.bugtracker.H3Medium
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.LabelLight
import com.significo.bugtracker.components.AppProgressLoading
import com.significo.bugtracker.components.AppTextField
import com.significo.bugtracker.core.ui.R

@Composable
fun ColumnScope.EditorContent(
    issue: IssueCreate,
    isSubmitting: Boolean,
    onIssueChanged: (IssueCreate) -> Unit
) {
    val tabs = listOf(
        stringResource(id = R.string.issue_create_tab_write),
        stringResource(id = R.string.issue_create_tab_preview)
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { LabelLight(tab) }
            )
        }
    }
    when (selectedTabIndex) {
        0 -> WriteContent(
            modifier = Modifier.fillMaxWidth(),
            issue = issue,
            isSubmitting = isSubmitting,
            onIssueChanged = onIssueChanged
        )

        1 -> PreviewContent(
            modifier = Modifier.fillMaxWidth(),
            issue = issue,
            isSubmitting = isSubmitting
        )
    }
}

@Composable
private fun WriteContent(
    issue: IssueCreate,
    isSubmitting: Boolean,
    onIssueChanged: (IssueCreate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimensions.Small)
            .verticalScroll(rememberScrollState())
    ) {
        AppTextField(
            value = issue.title,
            placeholder = stringResource(id = R.string.issue_create_title_placeholder),
            onValueChange = {
                onIssueChanged(issue.copy(title = it.trimIndent()))
            },
            singleLine = false,
            maxChars = 256,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimensions.Small))
        AppTextField(
            value = issue.body.orEmpty(),
            placeholder = stringResource(id = R.string.issue_create_body_placeholder),
            onValueChange = {
                onIssueChanged(issue.copy(body = it.trimIndent()))
            },
            singleLine = false,
            maxChars = 65536,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 200.dp)
        )
        Spacer(modifier = Modifier.height(Dimensions.Small))
        AppRichText(
            text = issue.getAttachedFilesMarkdown(context)
        )
        if (isSubmitting) {
            AttachingFilesContent()
        }
    }
}

@Composable
private fun PreviewContent(
    issue: IssueCreate,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bodyWithAttachedFiles = issue.getBodyWithAttachedFiles(context)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimensions.Medium)
            .verticalScroll(rememberScrollState())
    ) {
        H1Medium(
            text = issue.title.ifBlank {
                "[" + stringResource(id = R.string.issue_create_title_placeholder) + "]"
            },
            color = if (issue.title.isBlank()) {
                AppTheme.colors.inactiveText
            } else {
                Color.Unspecified
            }
        )
        Spacer(modifier = Modifier.height(Dimensions.Small))
        if (bodyWithAttachedFiles.isBlank()) {
            H3Medium(
                text = "[" + stringResource(id = R.string.issue_create_body_placeholder) + "]",
                color = if (bodyWithAttachedFiles.isBlank()) {
                    AppTheme.colors.inactiveText
                } else {
                    Color.Unspecified
                }
            )
        } else {
            AppRichText(text = bodyWithAttachedFiles)
        }
        if (isSubmitting) {
            AttachingFilesContent()
        }
    }
}

@Composable
private fun AttachingFilesContent() {
    Spacer(modifier = Modifier.height(Dimensions.Small))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        LabelLight(text = stringResource(id = R.string.issue_create_attaching_files))
        Spacer(modifier = Modifier.width(Dimensions.ExtraSmall))
        AppProgressLoading(
            size = Dimensions.Small,
            strokeWidth = 1.dp,
            color = AppTheme.colors.inactiveTint,
            centered = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewSuccess() {
    AppTheme {
        Column {
            EditorContent(
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
                ),
                isSubmitting = true,
                onIssueChanged = {}
            )
        }
    }
}
