package com.significo.bugtracker.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.ButtonDefaults.elevatedButtonElevation
import androidx.compose.material3.ButtonDefaults.textButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.CopyLight
import com.significo.bugtracker.CopyMedium

@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    submitting: Boolean = false
) {
    CompositionLocalProvider(LocalRippleTheme provides PrimaryButtonRippleTheme) {
        TextButton(
            onClick = onClick,
            modifier = modifier
                .requiredSizeIn(minHeight = LocalViewConfiguration.current.minimumTouchTargetSize.height),
            enabled = enabled && submitting.not(),
            elevation = elevatedButtonElevation(
                defaultElevation = 0.dp,
                pressedElevation = AppTheme.elevation.pressed,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ),
            colors = textButtonColors(
                containerColor = AppTheme.colors.secondaryText,
                contentColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = AppTheme.colors.inactiveTint,
                disabledContentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            if (submitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.surface
                )
            } else {
                CopyLight(
                    text = text,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    submitting: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .requiredSizeIn(minHeight = LocalViewConfiguration.current.minimumTouchTargetSize.height),
        enabled = enabled && submitting.not(),
        border = BorderStroke(
            width = 2.dp,
            color = if (enabled) AppTheme.colors.secondaryText else AppTheme.colors.inactiveTint
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = AppTheme.colors.primaryText,
            disabledContentColor = AppTheme.colors.inactiveTint
        )
    ) {
        if (submitting) {
            AppProgressLoading(
                size = 24.dp,
                color = AppTheme.colors.inactiveTint
            )
        } else {
            CopyLight(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AppTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = textButtonColors(disabledContentColor = AppTheme.colors.inactiveTint),
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun AppTertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues()
) {
    AppTertiaryButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding
    ) {
        CopyMedium(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AppSelectableButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp),
        enabled = enabled,
        elevation = buttonElevation(
            defaultElevation = if (selected) 0.dp else AppTheme.elevation.default,
            pressedElevation = AppTheme.elevation.pressed,
            hoveredElevation = if (selected) 0.dp else AppTheme.elevation.default,
            focusedElevation = if (selected) 0.dp else AppTheme.elevation.default,
            disabledElevation = AppTheme.elevation.default
        ),
        shape = MaterialTheme.shapes.medium,
        colors = textButtonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = if (selected) MaterialTheme.colorScheme.primary else AppTheme.colors.inactiveTint,
            disabledContentColor = if (selected) MaterialTheme.colorScheme.onPrimary else AppTheme.colors.secondaryText
        )
    ) {
        CopyLight(
            text = text,
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterStart)
        )
    }
}

private object PrimaryButtonRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = MaterialTheme.colorScheme.surface

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        contentColor = MaterialTheme.colorScheme.surface,
        lightTheme = !isSystemInDarkTheme()
    )
}

@Preview("Primary")
@Composable
private fun PrimaryPreview() {
    AppTheme {
        AppPrimaryButton(
            text = "Enabled",
            onClick = {}
        )
    }
}

@Preview("Primary")
@Composable
private fun PrimaryDisabledPreview() {
    AppTheme {
        AppPrimaryButton(
            text = "Disabled",
            onClick = {},
            enabled = false
        )
    }
}

@Preview("Primary")
@Composable
private fun PrimarySubmittingPreview() {
    AppTheme {
        AppPrimaryButton(
            text = "Submitting",
            onClick = {},
            submitting = true
        )
    }
}

@Preview("Secondary")
@Composable
private fun SecondaryPreview() {
    AppTheme {
        Surface {
            AppSecondaryButton(
                text = "Enabled",
                onClick = {}
            )
        }
    }
}

@Preview("Secondary")
@Composable
private fun SecondaryDisabledPreview() {
    AppTheme {
        Surface {
            AppSecondaryButton(
                text = "Disabled",
                onClick = {},
                enabled = false
            )
        }
    }
}

@Preview("Secondary")
@Composable
private fun SecondarySubmittingPreview() {
    AppTheme {
        Surface {
            AppSecondaryButton(
                text = "Submitting",
                onClick = {},
                submitting = true
            )
        }
    }
}

@Preview("Tertiary")
@Composable
private fun TertiaryPreview() {
    AppTheme {
        Surface {
            AppTertiaryButton(
                text = "Enabled",
                onClick = {}
            )
        }
    }
}

@Preview("Tertiary")
@Composable
private fun TertiaryDisabledPreview() {
    AppTheme {
        Surface {
            AppTertiaryButton(
                text = "Disabled",
                onClick = {},
                enabled = false
            )
        }
    }
}

@Preview("Selectable")
@Composable
private fun SelectablePreview() {
    AppTheme {
        AppSelectableButton(
            text = "Enabled",
            onClick = {}
        )
    }
}

@Preview("Selectable")
@Composable
private fun SelectableDisabledPreview() {
    AppTheme {
        AppSelectableButton(
            text = "Disabled",
            onClick = {},
            enabled = false
        )
    }
}

@Preview("Selectable")
@Composable
private fun SelectableSelectedPreview() {
    AppTheme {
        AppSelectableButton(
            text = "Selected",
            onClick = {},
            selected = true
        )
    }
}
