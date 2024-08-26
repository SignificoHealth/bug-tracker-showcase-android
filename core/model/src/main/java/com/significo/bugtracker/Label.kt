package com.significo.bugtracker

data class Label(
    val id: Long,
    val name: String,
    val description: String?,
    val color: String?
)
