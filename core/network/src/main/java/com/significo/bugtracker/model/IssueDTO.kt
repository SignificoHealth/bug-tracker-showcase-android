package com.significo.bugtracker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IssueDTO(
    val id: Long,
    val number: Int,
    val state: IssueStateDTO,
    val title: String,
    val body: String?,
    val user: UserDTO?,
    val labels: List<LabelDTO>,
    val assignee: UserDTO?,
    val assignees: List<UserDTO>?,
    val comments: Int,
    @Json(name = "closed_at")
    val closedAt: String?,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String
)
