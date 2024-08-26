package com.significo.bugtracker.feature.issue.create.ui

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.significo.bugtracker.CreateFileResponse
import com.significo.bugtracker.FileContent
import com.significo.bugtracker.Issue
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.State
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.repository.GitHubRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class IssueCreateViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: GitHubRepository
    private lateinit var viewModel: IssueCreateViewModel
    private val logger = mock(Logger::class.java)
    private val context = mock(Context::class.java)
    private val uri: Uri = mock(Uri::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(GitHubRepository::class.java)
        whenever(uri.toString()).thenReturn("content://dummy/path/to/file")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateFile updates the issue with the attached file`() = runTest {
        whenever(repository.uploadFile(uri)).thenReturn(createFileResponse)

        viewModel = IssueCreateViewModel(context, repository, logger)
        viewModel.uploadFile(uri)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(createFileResponse.content, state.issue.attachedFiles.first())
        }
    }

    @Test
    fun `updateFile updates the uiState with the error when repository throws exception`() = runTest {
        val exception = RuntimeException("Error")
        whenever(repository.uploadFile(uri)).thenThrow(exception)
        viewModel = IssueCreateViewModel(context, repository, logger)
        viewModel.uploadFile(uri)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.error != null)
        }

        verify(logger).log(exception)
    }

    @Test
    fun `createIssue creates an issue and calls to onSuccess`() = runTest {
        whenever(repository.createIssue(any())).thenReturn(issue)
        var isIssueCreatedSuccessfully = false

        viewModel = IssueCreateViewModel(context, repository, logger)
        viewModel.onIssueChanged(issueCreate)
        viewModel.createIssue(
            onSuccess = { isIssueCreatedSuccessfully = true }
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.hasChanges)
            assertTrue(isIssueCreatedSuccessfully)
        }
    }

    @Test
    fun `createIssue updates the uiState with the error when repository throws exception`() = runTest {
        val exception = RuntimeException("Error")
        whenever(repository.createIssue(any())).thenThrow(exception)
        var isIssueCreatedSuccessfully = false

        viewModel = IssueCreateViewModel(context, repository, logger)
        viewModel.onIssueChanged(issueCreate)
        viewModel.createIssue(
            onSuccess = { isIssueCreatedSuccessfully = true }
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.error != null)
            assertFalse(isIssueCreatedSuccessfully)
        }

        verify(logger).log(exception)
    }

    companion object {
        private const val FILENAME = "filename.pdf"
        private val createFileResponse = CreateFileResponse(
            content = FileContent(
                name = FILENAME,
                path = "path/to/$FILENAME",
                sha = "131234234234234",
                size = 2295,
                url = "https://path/to/$FILENAME",
                htmlUrl = "http://path/to/$FILENAME",
                gitUrl = "http://path/to/$FILENAME",
                downloadUrl = "https://path/to/$FILENAME",
                type = "pdf"
            )
        )
        private val issue = Issue(
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
        private val issueCreate = IssueCreate(
            title = issue.title,
            body = issue.body,
            attachedFiles = setOf(createFileResponse.content),
            bodyWithAttachedFiles = null
        )
    }
}
