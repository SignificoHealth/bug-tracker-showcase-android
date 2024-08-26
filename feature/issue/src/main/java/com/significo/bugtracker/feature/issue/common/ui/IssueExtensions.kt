package com.significo.bugtracker.feature.issue.common.ui

import android.content.Context
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.core.ui.R

fun IssueCreate.getAttachedFilesMarkdown(context: Context): String {
    val filesLinksMarkdown = this.attachedFiles.joinToString(separator = "\n") {
        "- [${it.name}](${it.htmlUrl})"
    }
    val attachedFiles = context.getString(R.string.issue_create_attached_files_section)
        .plus("\n\n")
        .plus(filesLinksMarkdown)
    return attachedFiles.takeIf { this.attachedFiles.isNotEmpty() }.orEmpty()
}

fun IssueCreate.getBodyWithAttachedFiles(context: Context): String {
    val body = this.body.orEmpty()
    val attachedFiles = this.getAttachedFilesMarkdown(context)
    return if (attachedFiles.isNotEmpty()) {
        "$body\n\n$attachedFiles"
    } else {
        body
    }
}
