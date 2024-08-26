package com.significo.bugtracker

data class Issue(
    val number: Int,
    val state: State,
    val title: String,
    val body: String?,
    val user: User?,
    val labels: List<Label>,
    val assignee: User?,
    val assignees: List<User>?,
    val comments: Int,
    val closedAt: String?,
    val createdAt: String,
    val updatedAt: String
)
