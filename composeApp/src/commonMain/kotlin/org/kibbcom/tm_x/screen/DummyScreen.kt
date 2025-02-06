package org.kibbcom.tm_x.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.platform.BackHandler


@Composable
fun DummyScreen(navigationState: NavigationNewState) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

        Text("This is my dummy screen ")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navigationState.navigateBack() }) {
            Text("Press Back")
        }
    }
}





