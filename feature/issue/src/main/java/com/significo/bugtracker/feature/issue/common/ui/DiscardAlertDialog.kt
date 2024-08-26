package com.significo.bugtracker.feature.issue.common.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.significo.bugtracker.AppTheme
import com.significo.bugtracker.CopyLight
import com.significo.bugtracker.CopyMedium
import com.significo.bugtracker.TextLight
import com.significo.bugtracker.core.ui.R

@Composable
fun DiscardAlertDialog(
    showDialog: MutableState<Boolean>,
    onDiscardChanges: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            CopyLight(text = stringResource(id = R.string.issue_create_dialog_discard_title))
        },
        text = {
            TextLight(stringResource(id = R.string.issue_create_dialog_discard_body))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                CopyMedium(text = stringResource(id = R.string.issue_create_dialog_discard_cancel))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                    onDiscardChanges()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                CopyMedium(text = stringResource(id = R.string.issue_create_dialog_discard_discard))
            }
        }
    )
}

@Preview
@Composable
fun DiscardAlertDialogPreview() {
    AppTheme {
        DiscardAlertDialog(
            showDialog = remember { mutableStateOf(true) },
            onDiscardChanges = {}
        )
    }
}
