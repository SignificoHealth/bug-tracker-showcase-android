package com.significo.bugtracker.feature.issues.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.paging.PagingDataHelper
import com.significo.bugtracker.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    logger: Logger
) : ViewModel() {

    private val pagingHelper = PagingDataHelper(
        scope = viewModelScope,
        apiCall = { _, page -> gitHubRepository.getIssues(page = page) },
        onTransform = { items -> items },
        logger = logger
    )
    val pagingData = pagingHelper.pagingData

    fun refresh() {
        pagingHelper.refreshPager()
    }
}
