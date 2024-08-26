package com.significo.bugtracker.extensions

import org.ocpsoft.prettytime.PrettyTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// ISO 8601 - RFC3339 format
private const val serverDateTimePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"

fun String.toLocalDateTime(): LocalDateTime {
    val temporalAccessor = DateTimeFormatter.ofPattern(serverDateTimePattern).parse(this)
    return LocalDateTime.from(temporalAccessor)
}

fun String.toPrettyTime(): String = PrettyTime().format(this.toLocalDateTime())
