package com.significo.bugtracker.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.H3Light
import com.significo.bugtracker.TextLight
import com.significo.bugtracker.core.ui.R

data class EmptyState(
    @StringRes val titleRes: Int,
    val description: String? = null,
    @DrawableRes val drawableRes: Int = R.drawable.ic_check_circle,
    @StringRes val ctaTextRes: Int? = null,
    @DrawableRes val ctaIconRes: Int? = null,
    val onCtaClick: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppEmptyState(
    /**
     * [EmptyState.drawableRes] modifier
     */
    modifier: Modifier = Modifier,
    emptyState: EmptyState,
    navigateBack: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        navigateBack?.let {
            AppTopBar(
                onActionClick = navigateBack,
                title = ""
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.Large)
                .semantics(mergeDescendants = true) { },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier.width(100.dp),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = emptyState.drawableRes),
                colorFilter = ColorFilter.tint(AppTheme.colors.primaryText),
                contentDescription = null
            )
            Spacer(Modifier.height(Dimensions.Large))

            H3Light(
                text = stringResource(emptyState.titleRes),
                textAlign = TextAlign.Center
            )

            emptyState.description?.let { description ->
                Spacer(Modifier.height(Dimensions.ExtraSmall))

                TextLight(
                    text = description,
                    textAlign = TextAlign.Center
                )
            }

            emptyState.onCtaClick?.let {
                Spacer(Modifier.height(Dimensions.Large))

                AppPrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = emptyState.ctaTextRes?.let {
                        stringResource(id = it)
                    }.orEmpty(),
                    onClick = emptyState.onCtaClick
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewWithoutCta() {
    AppEmptyState(
        emptyState = EmptyState(
            titleRes = R.string.empty_content_title_default,
            description = stringResource(R.string.empty_content_body_default),
            drawableRes = R.drawable.ic_check_circle
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewWithoutDescription() {
    AppEmptyState(
        emptyState = EmptyState(
            titleRes = R.string.empty_content_title_default,
            drawableRes = R.drawable.ic_check_circle,
            ctaTextRes = R.string.empty_content_cta_default,
            onCtaClick = { }
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewFull() {
    AppEmptyState(
        emptyState = EmptyState(
            titleRes = R.string.empty_content_title_default,
            description = stringResource(R.string.empty_content_body_default),
            drawableRes = R.drawable.ic_check_circle,
            ctaTextRes = R.string.empty_content_cta_default,
            onCtaClick = { }
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewNavBar() {
    AppEmptyState(
        emptyState = EmptyState(
            titleRes = R.string.empty_content_title_default,
            description = stringResource(R.string.empty_content_body_default),
            drawableRes = R.drawable.ic_check_circle,
            ctaTextRes = R.string.empty_content_cta_default,
            onCtaClick = { }
        ),
        navigateBack = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewSmallImage() {
    AppEmptyState(
        modifier = Modifier
            .size(70.dp)
            .aspectRatio(1f),
        emptyState = EmptyState(
            titleRes = R.string.empty_content_title_default,
            description = stringResource(R.string.empty_content_body_default),
            drawableRes = R.drawable.ic_check_circle,
            ctaTextRes = R.string.empty_content_cta_default,
            onCtaClick = { }
        ),
        navigateBack = {}
    )
}
