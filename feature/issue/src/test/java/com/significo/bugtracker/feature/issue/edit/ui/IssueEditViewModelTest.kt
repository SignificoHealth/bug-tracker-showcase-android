package com.significo.bugtracker.feature.issue.edit.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.significo.bugtracker.Issue
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.State
import com.significo.bugtracker.feature.issue.detail.navigation.IdArg
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.repository.GitHubRepository
import junit.framework.TestCase.assertEquals
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
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class IssueEditViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: GitHubRepository
    private lateinit var viewModel: IssueEditViewModel
    private val logger = mock(Logger::class.java)
    private val savedStateHandle = mock(SavedStateHandle::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(GitHubRepository::class.java)
        whenever(savedStateHandle.get<Int>(IdArg)).thenReturn(1)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchData updates uiState to Success when repository returns issue`() = runTest {
        whenever(repository.getIssue(anyInt())).thenReturn(issue)

        viewModel = IssueEditViewModel(savedStateHandle, repository, logger)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is IssueEditUiState.Success)
            assertEquals(issueCreate.title, (state as IssueEditUiState.Success).issue.title)
            assertEquals(issueCreate.body, state.issue.body)
        }
    }

    @Test
    fun `fetchData updates uiState to Error when repository throws exception`() = runTest {
        val exception = RuntimeException("Error")
        whenever(repository.getIssue(anyInt())).thenThrow(exception)
        viewModel = IssueEditViewModel(savedStateHandle, repository, logger)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is IssueEditUiState.Error)
            assertEquals(exception, (state as IssueEditUiState.Error).cause)
        }

        verify(logger).log(exception)
    }

    @Test
    fun `updateIssue updates an issue and calls to onSuccess`() = runTest {
        whenever(repository.getIssue(anyInt())).thenReturn(issue)
        whenever(repository.updateIssue(any(), any())).thenReturn(issue)
        var isIssueCreatedSuccessfully = false

        viewModel = IssueEditViewModel(savedStateHandle, repository, logger)
        viewModel.onIssueChanged(issueCreate)
        viewModel.updateIssue(
            onSuccess = { isIssueCreatedSuccessfully = true }
        )

        assertTrue(isIssueCreatedSuccessfully)
    }

    @Test
    fun `updateIssue updates the uiState with the error when repository throws exception`() = runTest {
        val exception = RuntimeException("Error")
        whenever(repository.getIssue(anyInt())).thenReturn(issue)
        whenever(repository.updateIssue(any(), any())).thenThrow(exception)
        var isIssueCreatedSuccessfully = false

        viewModel = IssueEditViewModel(savedStateHandle, repository, logger)
        viewModel.onIssueChanged(issueCreate)
        viewModel.updateIssue(
            onSuccess = { isIssueCreatedSuccessfully = true }
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue((state as IssueEditUiState.Success).isSubmittingError)
            assertFalse(isIssueCreatedSuccessfully)
        }

        verify(logger).log(exception)
    }

    companion object {
        private val issue = Issue(
            number = 1,
            state = State.OPEN,
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
        )
        private val issueCreate = IssueCreate(
            title = issue.title,
            body = issue.body,
            attachedFiles = setOf(),
            bodyWithAttachedFiles = null
        )
    }
}
