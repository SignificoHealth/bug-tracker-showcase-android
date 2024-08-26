package com.significo.bugtracker.paging.pagingtransformer.transformations

import androidx.paging.PagingData
import androidx.paging.map
import com.significo.bugtracker.paging.pagingtransformer.PagingDataTransformation

data class EditItem<ContentType : Any>(
    val itemMatcher: (ContentType) -> Boolean,
    val itemTransformer: (ContentType) -> ContentType
) : PagingDataTransformation<ContentType> {
    override fun apply(pagingData: PagingData<ContentType>): PagingData<ContentType> = pagingData.map {
        if (itemMatcher.invoke(it)) {
            return@map itemTransformer.invoke(it)
        } else {
            return@map it
        }
    }
}
