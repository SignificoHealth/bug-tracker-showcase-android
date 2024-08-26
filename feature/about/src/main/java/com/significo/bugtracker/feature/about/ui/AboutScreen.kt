package com.significo.bugtracker.feature.about.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.significo.bugtracker.AppRichText
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.components.AppTopBar
import com.significo.bugtracker.core.ui.R

@Composable
fun AboutRoute(contentWindowInsets: WindowInsets = WindowInsets.navigationBars) {
    AboutScreen(
        contentWindowInsets = contentWindowInsets
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(contentWindowInsets: WindowInsets = WindowInsets.navigationBars) {
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(id = R.string.tab_about))
        },
        contentWindowInsets = contentWindowInsets
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            AppRichText(
                modifier = Modifier.padding(Dimensions.Medium),
                text = stringResource(id = R.string.about_content)
            )
        }
    }
}
