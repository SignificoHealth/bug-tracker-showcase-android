package com.significo.bugtracker.extensions

import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * The runSuspendCatching will catch any exception and wrap it in a Result, except for
 * CancellationExceptions that will be re-thrown instead.
 * This will prevent unwanted interference with the cancellation handling of Coroutines.
 *
 * Ref: https://github.com/Kotlin/kotlinx.coroutines/issues/1814
 */
inline fun <R> runSuspendCatching(block: () -> R): Result<R> = try {
    Result.success(block())
} catch (t: TimeoutCancellationException) {
    Result.failure(t)
} catch (c: CancellationException) {
    throw c
} catch (e: Throwable) {
    Result.failure(e)
}
