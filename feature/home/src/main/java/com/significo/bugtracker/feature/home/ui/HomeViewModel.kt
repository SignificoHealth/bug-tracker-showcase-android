package com.significo.bugtracker.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.significo.bugtracker.User
import com.significo.bugtracker.extensions.runSuspendCatching
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
class HomeViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val logger: Logger
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = HomeUiState.Loading
    )

    init {
        fetchData()
    }

    fun fetchData() {
        _uiState.update { HomeUiState.Loading }
        viewModelScope.launch {
            runSuspendCatching {
                gitHubRepository.getUser()
            }.onSuccess { user ->
                _uiState.update { HomeUiState.Success(user) }
            }.onFailure { error ->
                logger.log(error)
                gitHubRepository.clearUser()
                _uiState.update { HomeUiState.Error(error) }
            }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val user: User) : HomeUiState
    data class Error(val cause: Throwable) : HomeUiState
}
