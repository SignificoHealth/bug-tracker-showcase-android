package com.significo.bugtracker.paging

data class ItemsPaginated<T>(
    val items: List<T>,
    val offset: Int,
    val total: Int
)
