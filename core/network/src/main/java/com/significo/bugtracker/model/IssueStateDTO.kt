package com.significo.bugtracker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class IssueStateDTO {
    @Json(name = "open")
    OPEN,

    @Json(name = "closed")
    CLOSED
}
