package com.significo.bugtracker.paging.pagingtransformer

import androidx.paging.PagingData

interface PagingDataTransformation<ContentType : Any> {
    fun apply(pagingData: PagingData<ContentType>): PagingData<ContentType>
}
