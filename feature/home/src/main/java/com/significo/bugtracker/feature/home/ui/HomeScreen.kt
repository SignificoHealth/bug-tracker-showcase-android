package com.significo.bugtracker.feature.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.significo.bugtracker.AppRichText
import com.significo.bugtracker.CopyMedium
import com.significo.bugtracker.Dimensions
import com.significo.bugtracker.User
import com.significo.bugtracker.components.AppErrorScreen
import com.significo.bugtracker.components.AppPrimaryButton
import com.significo.bugtracker.components.AppProgressLoading
import com.significo.bugtracker.components.AppTopBar
import com.significo.bugtracker.components.annotatedStringResource
import com.significo.bugtracker.core.ui.R

@Composable
fun HomeRoute(
    navigateToIssues: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState.value,
        onRetry = viewModel::fetchData,
        navigateToIssues = navigateToIssues
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onRetry: () -> Unit,
    navigateToIssues: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(id = R.string.app_name))
        },
        contentWindowInsets = contentWindowInsets
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                HomeUiState.Loading -> AppProgressLoading()
                is HomeUiState.Success -> HomeContent(
                    user = uiState.user,
                    navigateToIssues = navigateToIssues
                )
                is HomeUiState.Error -> AppErrorScreen(retry = onRetry)
            }
        }
    }
}

@Composable
fun HomeContent(
    user: User,
    navigateToIssues: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.Medium)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CopyMedium(text = annotatedStringResource(id = R.string.user_welcome, user.login))
        Spacer(Modifier.height(Dimensions.Medium))
        AppRichText(text = stringResource(id = R.string.app_description))
        Spacer(Modifier.height(Dimensions.Medium))
        AppPrimaryButton(
            text = stringResource(id = R.string.home_start_cta),
            onClick = { navigateToIssues.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.Medium)
        )
    }
}
