package org.kibbcom.tm_x.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.platform.BackHandler


@Composable
fun DeviceDetailScreen(navigationState: NavigationNewState, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues)
            .background(MaterialTheme.colorScheme.background), // Uses M3 background color
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

    }
}