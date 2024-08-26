package com.significo.bugtracker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    val id: Long,
    val name: String?,
    val email: String?,
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)
