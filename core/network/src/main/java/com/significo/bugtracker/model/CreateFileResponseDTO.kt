package com.significo.bugtracker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateFileResponseDTO(@Json(name = "content") val content: FileContentDTO)
