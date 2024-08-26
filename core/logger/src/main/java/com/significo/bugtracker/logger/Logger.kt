package com.significo.bugtracker.logger

interface Logger {
    fun log(
        error: Throwable,
        tag: String? = null,
        msg: String? = null
    )
}
