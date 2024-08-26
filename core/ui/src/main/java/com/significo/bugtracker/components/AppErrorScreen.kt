package com.significo.bugtracker.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.significo.bugtracker.core.ui.R

@Composable
fun AppErrorScreen(retry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics(mergeDescendants = true) { },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_cloud_off),
            colorFilter = ColorFilter.tint(AppTheme.colors.primaryText),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.width(100.dp),
            contentDescription = null
        )
        Spacer(Modifier.height(Dimensions.Large))
        H3Light(
            modifier = Modifier
                .padding(horizontal = Dimensions.ExtraLarge)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.error_message_unknown)
        )
        Spacer(Modifier.height(Dimensions.Medium))
        AppPrimaryButton(
            onClick = retry,
            text = stringResource(id = R.string.retry),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.Medium)
        )
        Spacer(Modifier.height(Dimensions.Medium))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppErrorScreen { }
}
