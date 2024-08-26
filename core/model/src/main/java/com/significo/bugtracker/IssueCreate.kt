package com.significo.bugtracker

data class IssueCreate(
    val title: String,
    val body: String?,
    val attachedFiles: Set<FileContent> = emptySet(),
    val bodyWithAttachedFiles: String? = null
) {
    companion object {
        val EMPTY = IssueCreate(
            title = "",
            body = null
        )
    }
}
