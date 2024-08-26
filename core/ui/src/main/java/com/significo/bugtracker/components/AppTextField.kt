package com.significo.bugtracker.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.CopyLight
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.LabelSmall

@ExperimentalMaterial3Api
private val colors: TextFieldColors
    @Composable
    get() = textFieldColors(
        disabledTextColor = AppTheme.colors.inactiveTint,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = AppTheme.colors.groupedBackground,
        disabledContainerColor = AppTheme.colors.inactiveTint,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        disabledLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedPlaceholderColor = AppTheme.colors.secondaryText,
        focusedPlaceholderColor = AppTheme.colors.secondaryText,
        disabledPlaceholderColor = AppTheme.colors.inactiveTint
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxChars: Int = Int.MAX_VALUE,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var text = value

    Column {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.groupedBackground,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.ExtraSmall),
                value = value,
                onValueChange = { newValue ->
                    text = newValue.take(maxChars)
                    onValueChange(text)
                },
                singleLine = singleLine,
                enabled = enabled,
                textStyle = AppTheme.typography.copyLight,
                decorationBox = { textField ->
                    TextFieldDefaults.DecorationBox(
                        value = value,
                        innerTextField = textField,
                        enabled = true,
                        singleLine = singleLine,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = remember { MutableInteractionSource() },
                        placeholder = { CopyLight(text = placeholder.orEmpty()) },
                        contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                            start = 0.dp,
                            top = 0.dp,
                            end = 0.dp,
                            bottom = 0.dp
                        ),
                        colors = colors
                    )
                },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions
            )
        }
        Spacer(modifier = Modifier.height(Dimensions.Tiny))
        LabelSmall(
            modifier = Modifier.padding(start = Dimensions.ExtraSmall),
            text = "${text.length} / $maxChars",
            color = if (text.length < maxChars) {
                AppTheme.colors.secondaryText
            } else {
                AppTheme.colors.error
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
fun ButtonTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(Dimensions.Small),
            verticalArrangement = Arrangement.spacedBy(Dimensions.Small)
        ) {
            AppTextField(
                value = "",
                onValueChange = {},
                placeholder = "Placeholder"
            )
        }
    }
}
