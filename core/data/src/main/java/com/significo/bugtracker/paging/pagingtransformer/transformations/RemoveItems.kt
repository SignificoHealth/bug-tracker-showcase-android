package com.significo.bugtracker.paging.pagingtransformer.transformations

import androidx.paging.PagingData
import androidx.paging.filter
import com.significo.bugtracker.paging.pagingtransformer.PagingDataTransformation

data class RemoveItems<ContentType : Any>(val itemMatcher: (ContentType) -> Boolean) : PagingDataTransformation<ContentType> {
    override fun apply(pagingData: PagingData<ContentType>): PagingData<ContentType> = pagingData.filter { item ->
        !itemMatcher(
            item
        )
    }
}
