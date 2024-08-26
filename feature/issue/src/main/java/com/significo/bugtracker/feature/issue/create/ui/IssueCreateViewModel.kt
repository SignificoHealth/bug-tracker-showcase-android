package com.significo.bugtracker.feature.issue.create.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.extensions.runSuspendCatching
import com.significo.bugtracker.feature.issue.common.ui.getBodyWithAttachedFiles
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class IssueCreateViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gitHubRepository: GitHubRepository,
    private val logger: Logger
) : ViewModel() {
    private val _uiState = MutableStateFlow(IssueCreateUiState(issue = IssueCreate.EMPTY))
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = IssueCreateUiState(issue = IssueCreate.EMPTY)
    )

    fun onIssueChanged(issue: IssueCreate) {
        _uiState.value = _uiState.value.copy(
            issue = issue,
            hasChanges = issue.hasChanges()
        )
    }

    fun createIssue(onSuccess: () -> Unit) {
        _uiState.value = _uiState.value.copy(
            isSubmitting = true,
            error = null
        )
        viewModelScope.launch {
            runSuspendCatching {
                val issue = _uiState.value.issue
                gitHubRepository.createIssue(
                    issueCreate = issue.copy(
                        bodyWithAttachedFiles = issue.getBodyWithAttachedFiles(context)
                    )
                )
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false
                )
                onSuccess()
            }.onFailure { error ->
                logger.log(error)
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = error
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun uploadFile(fileUri: Uri) {
        _uiState.value = _uiState.value.copy(
            isSubmitting = true,
            error = null
        )
        viewModelScope.launch {
            runSuspendCatching {
                val response = gitHubRepository.uploadFile(fileUri)
                val issue = _uiState.value.issue
                issue.copy(
                    attachedFiles = issue.attachedFiles.plus(response.content)
                )
            }.onSuccess { issueUpdated ->
                _uiState.value = _uiState.value.copy(
                    issue = issueUpdated,
                    hasChanges = issueUpdated.hasChanges(),
                    isSubmitting = false
                )
            }.onFailure { error ->
                logger.log(error)
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = error
                )
            }
        }
    }

    private fun IssueCreate.hasChanges(): Boolean = this.title.isNotBlank() && this != IssueCreate.EMPTY
}

data class IssueCreateUiState(
    val issue: IssueCreate,
    val hasChanges: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: Throwable? = null
)
