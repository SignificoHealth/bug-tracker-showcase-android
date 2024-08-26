package com.significo.bugtracker.paging.pagingtransformer.transformations

import androidx.paging.PagingData
import androidx.paging.insertFooterItem
import com.significo.bugtracker.paging.pagingtransformer.PagingDataTransformation

data class InsertFooterItem<ContentType : Any>(val newItem: ContentType) : PagingDataTransformation<ContentType> {
    override fun apply(pagingData: PagingData<ContentType>): PagingData<ContentType> = pagingData.insertFooterItem(item = newItem)
}
