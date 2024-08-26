package com.significo.bugtracker.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LabelDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val color: String?
)
