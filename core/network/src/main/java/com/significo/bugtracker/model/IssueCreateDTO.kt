package com.significo.bugtracker.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IssueCreateDTO(
    val title: String,
    val body: String?
)
