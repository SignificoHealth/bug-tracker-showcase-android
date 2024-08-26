package com.significo.bugtracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.paging.setupLoadStates
import kotlinx.coroutines.flow.flowOf

/**
 * @param isEmpty Use this parameter only in case you need additional logic to determine when
 * to show the empty state, e.g: when the screen show different sections, not only the paginated one.
 * @param isLoading Use only in case there are other external views apart from the paginated list
 * that needs to be loaded in order to show the loading view.
 */
@Composable
fun AppScreenStateAwarePaginatedList(
    modifier: Modifier = Modifier,
    emptyStateModifier: Modifier = Modifier,
    items: LazyPagingItems<*>,
    scrollState: LazyListState = rememberLazyListState(),
    isLoading: Boolean = false,
    enablePullToRefresh: Boolean = true,
    avoidClickingWhenRefreshing: Boolean = true,
    isEmpty: Boolean? = null,
    // Deprecated, please use emptyStateContent instead
    emptyState: EmptyState? = null,
    emptyStateContent: (@Composable () -> Unit)? = null,
    retry: () -> Unit,
    throwable: Throwable? = null,
    colorStatusBar: Color = MaterialTheme.colorScheme.background,
    headerContent: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: LazyListScope.() -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(false)
    val showLoadingIndicator = remember(items.loadState.refresh, isLoading) {
        items.loadState.refresh is LoadState.Loading || isLoading
    }
    swipeRefreshState.isRefreshing = showLoadingIndicator

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        headerContent?.invoke()

        when {
            throwable != null -> {
                AppErrorScreen(retry = retry)
            }

            else -> {
                SwipeRefreshContent(
                    swipeRefreshState = swipeRefreshState,
                    enablePullToRefresh = enablePullToRefresh,
                    avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                    retry = retry,
                    colorStatusBar = colorStatusBar
                ) {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        contentPadding = contentPadding ?: PaddingValues(0.dp),
                        verticalArrangement = verticalArrangement,
                        state = scrollState
                    ) {
                        content()

                        setupLoadStates(
                            pagingItems = items,
                            isEmptyState = isEmpty,
                            emptySate = emptyStateContent ?: emptyState?.let {
                                {
                                    AppEmptyState(
                                        modifier = emptyStateModifier,
                                        emptyState = emptyState
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SwipeRefreshContent(
    enablePullToRefresh: Boolean,
    swipeRefreshState: SwipeRefreshState,
    retry: () -> Unit,
    avoidClickingWhenRefreshing: Boolean,
    colorStatusBar: Color,
    content: @Composable () -> Unit
) {
    AppTheme(colorStatusBar = colorStatusBar) {
        SwipeRefresh(
            swipeEnabled = enablePullToRefresh,
            state = swipeRefreshState,
            onRefresh = { retry() },
            indicator = { state, refreshTrigger ->
                when {
                    !enablePullToRefresh -> {
                        AppSwipeRefreshLoadingIndicator(
                            state = state,
                            refreshTrigger = refreshTrigger,
                            elevation = 0.dp
                        )
                    }

                    avoidClickingWhenRefreshing && state.isRefreshing -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AppSwipeRefreshLoadingIndicator(state, refreshTrigger)
                        }
                    }

                    else -> {
                        AppSwipeRefreshLoadingIndicator(state, refreshTrigger)
                    }
                }
            },
            content = content
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PaginatedPreview() {
    AppScreenStateAwarePaginatedList(
        items = flowOf(PagingData.from(listOf("Item"))).collectAsLazyPagingItems(),
        retry = { }
    ) {
        item {
            Text(text = "Some content")
        }
    }
}
