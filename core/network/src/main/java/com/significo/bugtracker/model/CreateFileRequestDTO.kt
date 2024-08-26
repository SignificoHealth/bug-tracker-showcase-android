package com.significo.bugtracker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateFileRequestDTO(
    @Json(name = "message") val message: String,
    @Json(name = "content") val content: String,
    @Json(name = "branch") val branch: String? = null
)
