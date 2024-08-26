package com.significo.bugtracker.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.significo.bugtracker.logger.Logger

private const val FIRST_PAGE = 1
private const val INITIAL_OFFSET = 0

internal class ItemsDataSource<T : Any, R : Any>(
    private val apiCall: suspend (offset: Int, page: Int) -> ItemsPaginated<T>,
    private val transform: suspend (offset: List<T>, total: Int) -> List<R>,
    private val logger: Logger
) : PagingSource<Int, R>() {
    private var offset = INITIAL_OFFSET
    private var page = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, R> = try {
        val response = apiCall(offset, page)
        offset += response.items.size
        page += 1

        LoadResult.Page(
            data = transform(response.items, response.total),
            prevKey = null,
            nextKey = if (response.items.isEmpty()) null else page
        )
    } catch (e: Exception) {
        logger.log(tag = ItemsDataSource::class.simpleName, error = e)
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, R>) = INITIAL_OFFSET
}
