package com.significo.bugtracker.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.paging.pagingtransformer.PagingDataFlowTransformer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

private const val PAGE_SIZE = 20

@ExperimentalCoroutinesApi
class PagingDataHelper<T : Any, R : Any>(
    scope: CoroutineScope,
    private var apiCall: suspend (offset: Int, page: Int) -> ItemsPaginated<T>,
    private val onTotalCountUpdated: (total: Int) -> Unit = { },
    private val onTransform: suspend (items: List<T>) -> List<R>,
    private val logger: Logger
) {
    private val pagingDataFlowTransformer = PagingDataFlowTransformer<R>()
    private var updateTotalCount: Boolean = true
    private val triggerPagingStateFlow = MutableStateFlow(false)
    val pagingData: Flow<PagingData<R>> =
        triggerPagingStateFlow
            .flatMapLatest {
                Pager(
                    config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = {
                        ItemsDataSource(
                            apiCall = { offset, page -> apiCall(offset, page) },
                            transform = { friends, total ->
                                if (updateTotalCount) {
                                    updateTotalCount = false
                                    onTotalCountUpdated(total)
                                }
                                onTransform(friends)
                            },
                            logger = logger
                        )
                    }
                ).flow
            }
            .cachedIn(scope).run {
                pagingDataFlowTransformer.bind(this)
            }

    fun refreshPager() {
        updateTotalCount = true
        pagingDataFlowTransformer.clearTransformationsList()
        triggerPagingStateFlow.value = !triggerPagingStateFlow.value
    }

    fun updateApiCall(newApiCall: suspend (Int, Int) -> ItemsPaginated<T>) {
        apiCall = newApiCall
        refreshPager()
    }

    fun removeItems(itemsMatcher: (item: R) -> Boolean) {
        pagingDataFlowTransformer.removeItems(itemsMatcher)
    }

    fun insertHeaderItem(item: R) {
        pagingDataFlowTransformer.insertHeaderItem(item)
    }

    fun editItem(
        itemMatcher: (item: R) -> Boolean,
        itemTransformer: (itemToBeChanged: R) -> R
    ) {
        pagingDataFlowTransformer.editItem(itemMatcher, itemTransformer)
    }
}
