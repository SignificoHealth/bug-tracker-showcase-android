package com.significo.bugtracker.extensions

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.significo.bugtracker.logger.Logger

fun Uri.readFileContent(
    context: Context,
    logger: Logger
): ByteArray? = try {
    context.contentResolver.openInputStream(this)?.use { inputStream ->
        inputStream.readBytes()
    }
} catch (e: Exception) {
    logger.log(e, "UriExtensions", "Error reading file content: $this")
    null
}

fun Uri.getFileName(context: Context): String? {
    val cursor = context.contentResolver.query(this, null, null, null, null)
    return cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
            it.getString(nameIndex)
        } else {
            null
        }
    }
}
