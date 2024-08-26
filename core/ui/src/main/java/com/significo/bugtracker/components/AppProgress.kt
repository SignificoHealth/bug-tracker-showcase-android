package com.significo.bugtracker.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.Dimensions

@Composable
fun AppProgressLoading(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = Dimensions.ExtraLarge,
    strokeWidth: Dp = 3.dp,
    centered: Boolean = true
) {
    if (centered) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            CircularProgressIndicator(
                modifier = modifier.size(size),
                color = color,
                strokeWidth = strokeWidth
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = modifier.size(size),
            color = color,
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun AppLinearProgress(
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = AppTheme.colors.inactiveTint,
    progress: Float? = null,
    animDuration: Int? = null,
    strokeCap: StrokeCap = StrokeCap.Butt,
    shape: Shape = RoundedCornerShape(0.dp)
) {
    val firstLoad = remember { mutableStateOf(true) }

    if (progress != null) {
        val progressAnimated = if (animDuration != null && !firstLoad.value) {
            animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(
                    durationMillis = animDuration,
                    easing = LinearEasing
                ),
                label = "LinearProgressIndicator"
            ).value
        } else {
            firstLoad.value = false
            progress
        }

        LinearProgressIndicator(
            progress = {
                progressAnimated
            },
            modifier = modifier
                .clip(shape = shape)
                .fillMaxWidth(),
            color = progressColor,
            trackColor = backgroundColor,
            strokeCap = strokeCap
        )
    } else {
        LinearProgressIndicator(
            progress = { 0.0f },
            modifier = modifier
                .clip(shape = shape)
                .fillMaxWidth(),
            color = progressColor,
            trackColor = AppTheme.colors.inactiveTint
        )
    }
}

@Composable
fun AppSwipeRefreshLoadingIndicator(
    state: SwipeRefreshState,
    refreshTrigger: Dp,
    elevation: Dp = 4.dp
) {
    val sizeDp = 48.dp
    val sizePx = with(LocalDensity.current) { sizeDp.toPx() }
    val triggerPx = with(LocalDensity.current) { refreshTrigger.toPx().toInt() }
    val isRefresh = state.indicatorOffset.toInt() > triggerPx

    val offset = when {
        isRefresh -> triggerPx + (state.indicatorOffset * .1f)
        state.isRefreshing -> triggerPx
        else -> state.indicatorOffset // is swiping
    }
    val elevationPx = with(LocalDensity.current) { elevation.toPx() }

    AppProgressLoading(
        modifier = Modifier
            .size(sizeDp)
            .offset { IntOffset(x = 0, y = -(sizePx + elevationPx).toInt()) }
            .offset { IntOffset(x = 0, y = offset.toInt()) }
    )
}

@Preview
@Composable
private fun AppProgressLoadingPreview() {
    AppProgressLoading()
}

@Preview
@Composable
private fun AppProgressLoadingPreviewNoCentered() {
    AppProgressLoading(centered = false)
}

@Preview(showBackground = true)
@Composable
private fun LinearProgressPreview() {
    Box(modifier = Modifier.padding(Dimensions.Medium)) {
        AppLinearProgress(progress = .5f)
    }
}

@Preview(showBackground = true)
@Composable
private fun LinearProgressPreviewDetail() {
    Box(modifier = Modifier.padding(Dimensions.Medium)) {
        AppLinearProgress(
            progress = .5f,
            strokeCap = StrokeCap.Round,
            progressColor = AppTheme.colors.success,
            backgroundColor = AppTheme.colors.inactiveTint,
            modifier = Modifier.height(20.dp),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Preview
@Composable
private fun AppSwipeRefreshLoadingIndicatorPreview() {
    AppSwipeRefreshLoadingIndicator(
        state = SwipeRefreshState(isRefreshing = true),
        refreshTrigger = 100.dp
    )
}
