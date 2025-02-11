package org.kibbcom.tm_x

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@Composable
fun BeaconScreen() {
    var isBeaconEnabled by remember { mutableStateOf(true) }
    var isScanning by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("Choose Type") }

    // Sample data for beacon devices
    val beaconDevices = remember {
        listOf(
            BeaconDevice("Sony JBL","12:90:889","Bsi23",563359987, 988989,true ),
            BeaconDevice("TM-X","12:90:889","Bsi23",563359987, 988989, false),
            BeaconDevice("Sony JBL","12:90:889","Bsi23",563359987, 988989, true),
            BeaconDevice("TM-X","12:90:889","Bsi23",563359987, 988989, true)
        )
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Beacon Control Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            backgroundColor = MaterialTheme.colors.surface,
            border = BorderStroke(1.dp, CardBorderColor),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    AsyncImage(
                        model = "https://images-platform.99static.com//CSsrGrz0M4afBGYk1e3W1iMEa6c=/1759x1132:2800x2173/fit-in/500x500/projects-files/160/16075/1607511/37cfbdae-164c-4d70-884f-c3d629db72c0.png",
                        contentDescription = "beacon",
                        modifier = Modifier
                            .size(50.dp) // Size of the image
                            .clip(RoundedCornerShape(10))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Beacon",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                Switch(
                    checked = isBeaconEnabled,
                    onCheckedChange = { isBeaconEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.secondary,
                        checkedTrackColor = MaterialTheme.colors.secondary.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }
        }

        if (isScanning) {
            Button(
                onClick = { isScanning = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = Color.White
                )
            ) {
                Text("Stop Scanning")
            }
        }

        // Buttons for type selection
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { selectedType = "Eddy Stone" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedType == "Eddy Stone") MaterialTheme.colors.primary else MaterialTheme.colors.surface
                )
            ) {
                Text("Eddy Stone")
            }

            Button(
                onClick = { selectedType = "Beacon" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedType == "Beacon") MaterialTheme.colors.primary else MaterialTheme.colors.surface
                )
            ) {
                Text("Beacon")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            items(beaconDevices) { beacon ->
                BeaconItem(beacon = beacon)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Add padding at the bottom to avoid hiding the last item
            item {
                Spacer(modifier = Modifier.height(80.dp)) // Adjust based on your bottom bar height
            }
        }


    }
}