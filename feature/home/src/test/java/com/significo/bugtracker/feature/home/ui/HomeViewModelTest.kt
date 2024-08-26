package com.significo.bugtracker.feature.home.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.significo.bugtracker.User
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.repository.GitHubRepository
import junit.framework.TestCase.assertEquals
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
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: GitHubRepository
    private lateinit var viewModel: HomeViewModel
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
    fun `fetchData updates uiState to Success when repository returns user`() = runTest {
        val user = User(id = 1, name = null, email = null, login = "username", avatarUrl = "")
        whenever(repository.getUser()).thenReturn(user)

        viewModel = HomeViewModel(repository, logger)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HomeUiState.Success)
            assertEquals(user, (state as HomeUiState.Success).user)
        }
    }

    @Test
    fun `fetchData updates uiState to Error when repository throws exception`() = runTest {
        val exception = RuntimeException("Error")
        whenever(repository.getUser()).thenThrow(exception)
        whenever(repository.clearUser()).thenReturn(Unit)
        viewModel = HomeViewModel(repository, logger)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HomeUiState.Error)
            assertEquals(exception, (state as HomeUiState.Error).cause)
        }

        verify(logger).log(exception)
        verify(repository).clearUser()
    }
}
