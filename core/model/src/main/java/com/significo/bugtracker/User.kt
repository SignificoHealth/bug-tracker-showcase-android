package com.significo.bugtracker

data class User(
    val id: Long,
    val name: String?,
    val email: String?,
    val login: String,
    val avatarUrl: String
)
