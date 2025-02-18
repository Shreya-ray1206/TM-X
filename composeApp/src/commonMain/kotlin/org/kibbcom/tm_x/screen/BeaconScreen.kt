package org.kibbcom.tm_x.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kibbcom.tm_x.db.AppDatabase
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.models.BeaconDevice
import org.kibbcom.tm_x.platform.BackHandler
import org.kibbcom.tm_x.platform.viewmodel.BeaconViewModelFactory
import org.kibbcom.tm_x.theme.getToolbarAdditionColor
import org.kibbcom.tm_x.viewmodel.BeaconViewModel
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.beacon


@Composable
fun BeaconScreen(db: AppDatabase, navigationState: NavigationNewState, paddingValues: PaddingValues,
                 viewModel: BeaconViewModel = viewModel(factory = BeaconViewModelFactory(db))
){


    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val rotationDegree = animateFloatAsState(
            targetValue = 360f, // Rotate to 360 degrees
            animationSpec = tween(durationMillis = 5000, easing = androidx.compose.animation.core.FastOutSlowInEasing)
        )

        var dotCount = remember { mutableStateOf(1) }
        var isAnimating = remember { mutableStateOf(true) }

        // Run the animation for 10 seconds
        LaunchedEffect(key1 = isAnimating.value) {
            var elapsedTime = 0
            while (elapsedTime < 10_000 && isAnimating.value) { // Stop after 10 seconds (10,000 ms)
                delay(500)  // 500 ms delay
                dotCount.value = (dotCount.value % 3) + 1 // Cycle between 1, 2, and 3 dots
                elapsedTime += 500
            }
            isAnimating.value = false // Stop the animation after 10 seconds
        }

        // Create the "Scanning" text with ellipsis
        val scanningText = "Scanning${".".repeat(dotCount.value)}"
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                    ambientColor = Color.White,
                    spotColor = Color.White
                )
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(getToolbarAdditionColor())
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically // Ensures vertical alignment of Image and Text
                ){

                    Image(
                        painter = painterResource(Res.drawable.beacon), // Replace with your image resource
                        contentDescription = "Scanning Icon",
                        modifier = Modifier
                            .size(50.dp).padding(5.dp)
                            .rotate(rotationDegree.value), // Apply rotation
                        colorFilter = ColorFilter.tint(Color.White) // Apply white tint (optional)
                    )

                    Text(
                        text = scanningText,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

            }

        }


        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

        LaunchedEffect(Unit) {
            viewModel.scanBeaconDevices()
        }


        val beaconDevice = remember {
            listOf(
                BeaconDevice("Sony JBL","12:90:889","RSSI-8" ),
                BeaconDevice("TM-4","12:90:89","RSSI-1"),
                BeaconDevice("OnePlus JBL","12:90:8","RSSI-4"),
                BeaconDevice("TM-3","12:90:88889","RSSI-2"),
                BeaconDevice("TM-5","12:420:75:88889","RSSI-11"),
                BeaconDevice("TM-7","12:45:120:88889","RSSI-12"),
                BeaconDevice("TM-9","12:15:145:889","RSSI-0")
            )
        }



        LazyColumn {
            items(beaconDevice) { device ->

                BeaconItem(device,viewModel)

            }
        }
    }
}


@Composable
fun BeaconItem(beacon: BeaconDevice, viewModel: BeaconViewModel) {


    val savedBeacons by viewModel.savedBeacons.collectAsState()

    val isDbSaved = savedBeacons.any { it.macAddress == beacon.macAddress } // Check if beacon is saved


    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(8.dp), // Keep all corners rounded at 25.dp
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // This will now use the theme value
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = beacon.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mac Address : ${beacon.macAddress}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Rssi : ${ beacon.rssi}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row() {
                Text(
                    text = "Major : ${beacon.major} Minor : ${beacon.minor}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(70.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {



                    // Connect Button
                    Button(
                        onClick = {
                            viewModel.saveBeacon(beacon)

                        },
                        enabled = !isDbSaved,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
                        ),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = if (isDbSaved) "Saved" else "Save",
                            color = if (isDbSaved) Color(0xFF88D66C) else Color.White
                        )
                    }

                }
            }
        }
    }


}