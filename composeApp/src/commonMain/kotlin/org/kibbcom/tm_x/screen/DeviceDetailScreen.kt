package org.kibbcom.tm_x.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.common.getToolbarAdditionColor
import org.kibbcom.tm_x.platform.BackHandler
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.bluetooth


@Composable
fun DeviceDetailScreen(navigationState: NavigationNewState, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues)
            .background(MaterialTheme.colorScheme.background), // Uses M3 background color
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = getCommonModifierForAdditionToolbar(40.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(getToolbarAdditionColor())
        ) {
        }


        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

    }
}