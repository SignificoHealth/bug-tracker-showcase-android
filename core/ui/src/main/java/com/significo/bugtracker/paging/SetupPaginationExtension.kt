package com.significo.bugtracker.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.TextLight
import com.significo.bugtracker.components.AppErrorScreen
import com.significo.bugtracker.components.AppPrimaryButton
import com.significo.bugtracker.components.AppProgressLoading
import com.significo.bugtracker.core.ui.R

fun LazyListScope.setupLoadStates(
    isVerticalList: Boolean = true,
    pagingItems: LazyPagingItems<*>,
    initialLoadingState: @Composable (() -> Unit)? = null,
    loadingState: @Composable (() -> Unit)? = null,
    initialErrorState: @Composable ((Throwable) -> Unit)? = null,
    errorState: @Composable ((Throwable) -> Unit)? = null,
    emptySate: @Composable (() -> Unit)? = null,
    isEmptyState: Boolean? = null
) {
    val defaultInitialErrorState: @Composable (Throwable) -> Unit = { throwable ->
        AppErrorScreen(
            retry = { pagingItems.retry() }
        )
    }

    setupPagingLoadStates(
        pagingItems = pagingItems,
        initialLoadingState = {
            item { initialLoadingState?.invoke() }
        },
        loadingState = {
            item {
                loadingState?.invoke() ?: LoadingStateViewContent(isVerticalList = isVerticalList)
            }
        },
        initialErrorState = { throwable ->
            item { initialErrorState?.invoke(throwable) ?: defaultInitialErrorState(throwable) }
        },
        errorState = { throwable ->
            item {
                errorState?.invoke(throwable) ?: ErrorStateViewContent(
                    callToActionButtonListener = { pagingItems.retry() }
                )
            }
        },
        emptySate = {
            item { emptySate?.invoke() }
        },
        isEmptyState = isEmptyState
    )
}

/**
 * @param isEmptyState Use this parameter only in case you need additional logic to determine when
 * to show the empty state, e.g: when the screen show different sections, not only the paginated one
 */
private fun setupPagingLoadStates(
    pagingItems: LazyPagingItems<*>,
    initialLoadingState: (() -> Unit)? = null,
    loadingState: () -> Unit,
    initialErrorState: (Throwable) -> Unit,
    errorState: (Throwable) -> Unit,
    emptySate: (() -> Unit)? = null,
    isEmptyState: Boolean? = null
) {
    pagingItems.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                initialLoadingState?.invoke()
            }

            loadState.append is LoadState.Loading -> {
                loadingState()
            }

            loadState.refresh is LoadState.Error -> {
                val throwable = (loadState.refresh as LoadState.Error).error
                initialErrorState(throwable)
            }

            loadState.append is LoadState.Error -> {
                val throwable = (loadState.append as LoadState.Error).error
                errorState(throwable)
            }

            loadState.refresh is LoadState.NotLoading -> {
                val isActuallyEmpty = pagingItems.itemCount == 0 &&
                    loadState.append.endOfPaginationReached
                val showEmptyState = (isEmptyState ?: true) && emptySate != null
                if (isActuallyEmpty && showEmptyState) {
                    emptySate!!()
                }
            }
        }
    }
}

@Composable
private fun ErrorStateViewContent(callToActionButtonListener: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.Small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextLight(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.error_message_unknown),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Dimensions.Small))

        AppPrimaryButton(
            onClick = { callToActionButtonListener() },
            text = stringResource(id = R.string.retry)
        )
    }
}

@Composable
private fun LoadingStateViewContent(isVerticalList: Boolean) {
    val topPadding = if (isVerticalList) Dimensions.Small else 0.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(topPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppProgressLoading(size = Dimensions.Medium)
    }
}
