package com.significo.bugtracker.feature.issues.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.testing.asSnapshot
import com.significo.bugtracker.Issue
import com.significo.bugtracker.State
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.paging.ItemsPaginated
import com.significo.bugtracker.repository.GitHubRepository
import junit.framework.TestCase.assertEquals
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
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class IssuesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: GitHubRepository
    private lateinit var viewModel: IssuesViewModel
    private val logger = mock(Logger::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(GitHubRepository::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchData updates uiState to Success when repository returns issues`() = runTest {
        val totalItems = 10
        val items = List(totalItems) { issue.copy(number = it) }
        whenever(repository.getIssues(page = 1)).thenReturn(
            ItemsPaginated(
                items = items,
                offset = totalItems,
                total = totalItems
            )
        )
        whenever(repository.getIssues(page = 2)).thenReturn(
            ItemsPaginated(
                items = emptyList(),
                offset = totalItems,
                total = totalItems
            )
        )
        viewModel = IssuesViewModel(repository, logger)
        val itemsSnapshot: List<Issue> = viewModel.pagingData.asSnapshot()
        assertEquals(items, itemsSnapshot)
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
            comments = 0,
            closedAt = null,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z"
        )
    }
}
