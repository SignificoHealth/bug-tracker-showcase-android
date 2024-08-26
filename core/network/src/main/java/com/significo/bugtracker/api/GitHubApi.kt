package com.significo.bugtracker.api

import com.significo.bugtracker.model.CreateFileRequestDTO
import com.significo.bugtracker.model.CreateFileResponseDTO
import com.significo.bugtracker.model.IssueCreateDTO
import com.significo.bugtracker.model.IssueDTO
import com.significo.bugtracker.model.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

const val GITHUB_BASE_URL = "https://api.github.com/"
private const val OWNER = "SignificoHealth"
private const val REPO = "bug-tracker-showcase-android"

interface GitHubApi {

    @GET("user")
    suspend fun getUser(): UserDTO

    @GET("repos/$OWNER/$REPO/issues")
    suspend fun listIssues(
        @Query("creator") creator: String? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("state") state: String? = null,
        @Query("sort") sort: String? = null
    ): List<IssueDTO>

    @GET("repos/$OWNER/$REPO/issues/{issue_number}")
    suspend fun getIssue(@Path("issue_number") issueNumber: Int): IssueDTO

    @POST("repos/$OWNER/$REPO/issues")
    suspend fun createIssue(@Body issueCreateDTO: IssueCreateDTO): IssueDTO

    @PATCH("repos/$OWNER/$REPO/issues/{issue_number}")
    suspend fun updateIssue(
        @Path("issue_number") issueNumber: Int,
        @Body issueCreateDTO: IssueCreateDTO
    ): IssueDTO

    @PUT("repos/$OWNER/$REPO/contents/{path}")
    suspend fun uploadFile(
        @Path("path") path: String,
        @Body request: CreateFileRequestDTO
    ): CreateFileResponseDTO
}
