package com.significo.bugtracker.paging.pagingtransformer.transformations

import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import com.significo.bugtracker.paging.pagingtransformer.PagingDataTransformation

data class InsertHeaderItem<ContentType : Any>(val newItem: ContentType) : PagingDataTransformation<ContentType> {
    override fun apply(pagingData: PagingData<ContentType>): PagingData<ContentType> = pagingData.insertHeaderItem(item = newItem)
}
