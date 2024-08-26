package com.significo.bugtracker.repository

import android.content.Context
import android.net.Uri
import com.significo.bugtracker.AppDataStore
import com.significo.bugtracker.CreateFileResponse
import com.significo.bugtracker.Issue
import com.significo.bugtracker.IssueCreate
import com.significo.bugtracker.User
import com.significo.bugtracker.api.GitHubApi
import com.significo.bugtracker.extensions.base64Encode
import com.significo.bugtracker.extensions.getFileName
import com.significo.bugtracker.extensions.readFileContent
import com.significo.bugtracker.logger.Logger
import com.significo.bugtracker.mapper.asDTO
import com.significo.bugtracker.mapper.asEntity
import com.significo.bugtracker.model.CreateFileRequestDTO
import com.significo.bugtracker.model.IssueStateDTO
import com.significo.bugtracker.paging.ItemsPaginated
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gitHubApi: GitHubApi,
    private val dataStore: AppDataStore,
    private val logger: Logger
) {
    suspend fun getUser(): User = gitHubApi.getUser().asEntity().also {
        dataStore.updateUser(it.login)
    }

    suspend fun clearUser() {
        dataStore.clearUser()
    }

    suspend fun createIssue(issueCreate: IssueCreate): Issue = gitHubApi.createIssue(issueCreate.asDTO()).asEntity()

    suspend fun updateIssue(
        issueNumber: Int,
        issueCreate: IssueCreate
    ): Issue = gitHubApi.updateIssue(
        issueNumber = issueNumber,
        issueCreateDTO = issueCreate.asDTO()
    ).asEntity()

    suspend fun getIssue(issueNumber: Int): Issue = gitHubApi.getIssue(issueNumber).asEntity()

    suspend fun getIssues(
        page: Int,
        perPage: Int = 30
    ): ItemsPaginated<Issue> {
        val response = gitHubApi.listIssues(
            creator = dataStore.user.first(),
            page = page,
            perPage = perPage,
            state = IssueStateDTO.OPEN.name.lowercase()
        )
        val offset = (page - 1) * perPage + response.size
        val total = offset + response.size

        return ItemsPaginated(
            items = response.map { it.asEntity() },
            offset = offset,
            total = total
        )
    }

    suspend fun uploadFile(fileUri: Uri): CreateFileResponse {
        val content = fileUri.readFileContent(context, logger)
        val path = "${System.currentTimeMillis()}-".plus(
            fileUri.getFileName(context) ?: fileUri.lastPathSegment
        )
        return gitHubApi.uploadFile(
            path = "attachments/$path",
            request = CreateFileRequestDTO(
                message = "Add file '$path'",
                content = content.base64Encode().orEmpty()
            )
        ).asEntity()
    }
}
