package com.significo.bugtracker.mapper

import com.significo.bugtracker.Issue
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.Label
import com.significo.bugtracker.State
import com.significo.bugtracker.User
import com.significo.bugtracker.model.IssueCreateDTO
import com.significo.bugtracker.model.IssueDTO
import com.significo.bugtracker.model.IssueStateDTO
import com.significo.bugtracker.model.LabelDTO
import com.significo.bugtracker.model.UserDTO

internal fun IssueDTO.asEntity() = Issue(
    number = number,
    state = state.asEntity(),
    title = title,
    body = body,
    user = user?.asEntity(),
    labels = labels.map { it.asEntity() },
    assignee = assignee?.asEntity(),
    assignees = assignees?.map { it.asEntity() },
    comments = comments,
    closedAt = closedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)

internal fun LabelDTO.asEntity() = Label(
    id = id,
    name = name,
    description = description,
    color = color
)

internal fun UserDTO.asEntity() = User(
    id = id,
    name = name,
    email = email,
    login = login,
    avatarUrl = avatarUrl
)

internal fun IssueStateDTO.asEntity() = when (this) {
    IssueStateDTO.OPEN -> State.OPEN
    IssueStateDTO.CLOSED -> State.CLOSED
}

internal fun IssueCreate.asDTO() = IssueCreateDTO(
    title = title,
    body = bodyWithAttachedFiles ?: body
)
