package org.kibbcom.tm_x.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.Screen
import org.kibbcom.tm_x.platform.BackHandler

@Composable
fun DummyScreen(navigationState: NavigationNewState, paddingValues : PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), // Apply padding from Scaffold
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

        Text("This is my Dummy screen ", color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navigationState.navigateBack() }) {
            Text("Press Back")
        }
    }
}

