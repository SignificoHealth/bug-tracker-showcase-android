package com.significo.bugtracker.feature.issue.edit.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.extensions.runSuspendCatching
import com.significo.bugtracker.feature.issue.edit.navigation.IssueEditArgs
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
class IssueEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gitHubRepository: GitHubRepository,
    private val logger: Logger
) : ViewModel() {
    private val args = IssueEditArgs(savedStateHandle)
    private var originalIssue = IssueCreate.EMPTY
    private var editIssue = IssueCreate.EMPTY

    private val _uiState = MutableStateFlow<IssueEditUiState>(IssueEditUiState.Loading)
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = IssueEditUiState.Loading
    )

    init {
        fetchData()
    }

    fun fetchData() {
        _uiState.update { IssueEditUiState.Loading }
        viewModelScope.launch {
            runSuspendCatching {
                gitHubRepository.getIssue(issueNumber = args.id)
            }.onSuccess { issue ->
                val issueCreate = IssueCreate(
                    title = issue.title,
                    body = issue.body
                )
                originalIssue = issueCreate
                editIssue = issueCreate
                _uiState.update { IssueEditUiState.Success(issue = issueCreate) }
            }.onFailure { error ->
                logger.log(error)
                _uiState.update { IssueEditUiState.Error(error) }
            }
        }
    }

    fun onIssueChanged(issue: IssueCreate) {
        editIssue = issue
        _uiState.update {
            IssueEditUiState.Success(
                issue = editIssue,
                hasChanges = editIssue.hasChanges()
            )
        }
    }

    fun updateIssue(onSuccess: () -> Unit) {
        _uiState.update {
            IssueEditUiState.Success(
                issue = editIssue,
                hasChanges = editIssue.hasChanges(),
                isSubmitting = true
            )
        }
        viewModelScope.launch {
            runSuspendCatching {
                gitHubRepository.updateIssue(
                    issueNumber = args.id,
                    issueCreate = editIssue
                )
            }.onSuccess {
                _uiState.update {
                    IssueEditUiState.Success(issue = editIssue)
                }
                onSuccess()
            }.onFailure { error ->
                logger.log(error)
                _uiState.update {
                    IssueEditUiState.Success(
                        issue = editIssue,
                        hasChanges = editIssue.hasChanges(),
                        isSubmittingError = true
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            IssueEditUiState.Success(
                issue = editIssue,
                hasChanges = editIssue.hasChanges()
            )
        }
    }

    private fun IssueCreate.hasChanges(): Boolean = this.title.isNotBlank() && this != originalIssue
}

sealed interface IssueEditUiState {
    data object Loading : IssueEditUiState
    data class Success(
        val issue: IssueCreate,
        val hasChanges: Boolean = false,
        val isSubmitting: Boolean = false,
        val isSubmittingError: Boolean = false
    ) : IssueEditUiState

    data class Error(val cause: Throwable) : IssueEditUiState
}
