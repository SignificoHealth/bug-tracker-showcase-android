package com.significo.bugtracker.mapper

import com.significo.bugtracker.CreateFileResponse
import com.significo.bugtracker.FileContent
import com.significo.bugtracker.model.CreateFileResponseDTO
import com.significo.bugtracker.model.FileContentDTO

internal fun FileContentDTO.asEntity() = FileContent(
    name = name,
    path = path,
    sha = sha,
    size = size,
    url = url,
    htmlUrl = htmlUrl,
    gitUrl = gitUrl,
    downloadUrl = downloadUrl,
    type = type
)

internal fun CreateFileResponseDTO.asEntity() = CreateFileResponse(
    content = content.asEntity()
)
