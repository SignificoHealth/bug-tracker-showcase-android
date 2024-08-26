package com.significo.bugtracker.feature.issue.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.significo.bugtracker.Issue
import com.significo.bugtracker.extensions.runSuspendCatching
import com.significo.bugtracker.feature.issue.detail.navigation.IssueArgs
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class IssueDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gitHubRepository: GitHubRepository,
    private val logger: Logger
) : ViewModel() {
    private val args = IssueArgs(savedStateHandle)

    private val _uiState = MutableStateFlow<IssueDetailUiState>(IssueDetailUiState.Loading)
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = IssueDetailUiState.Loading
    )

    init {
        fetchData()
    }

    fun fetchData() {
        _uiState.update { IssueDetailUiState.Loading }
        viewModelScope.launch {
            runSuspendCatching {
                gitHubRepository.getIssue(issueNumber = args.id)
            }.onSuccess { issue ->
                _uiState.update { IssueDetailUiState.Success(issue) }
            }.onFailure { error ->
                logger.log(error)
                _uiState.update { IssueDetailUiState.Error(error) }
            }
        }
    }
}

sealed interface IssueDetailUiState {
    data object Loading : IssueDetailUiState
    data class Success(val issue: Issue) : IssueDetailUiState
    data class Error(val cause: Throwable) : IssueDetailUiState
}
