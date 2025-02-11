package org.kibbcom.tm_x.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.Screen
import org.kibbcom.tm_x.models.BeaconDevice
import org.kibbcom.tm_x.platform.BackHandler
import org.kibbcom.tm_x.platform.getTmxDatabase
import org.kibbcom.tm_x.theme.CardBorderColor
import tm_x.composeapp.generated.resources.Res


@Composable
fun BeaconScreen(  navigationState: NavigationNewState,paddingValues: PaddingValues){


    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        val dao = getTmxDatabase().beaconDao();

        val people by dao.getAllPeople().collectAsState(initial = emptyList())

        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

        println( "All the saved data $people")
        var isBeaconEnabled by remember { mutableStateOf(true) }
        var isScanning by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }
        var selectedType by remember { mutableStateOf("Choose Type") }

        // Sample data for beacon devices

        val beaconDevices = remember {
            listOf(
             //   BeaconDevice("Sony JBL","12:90:889","Bsi23",563359987, 988989,true ),
               // BeaconDevice("TM-X","12:90:889","Bsi23",563359987, 988989, false),
               // BeaconDevice("Sony JBL","12:90:889","Bsi23",563359987, 988989, true),
                BeaconDevice("TM-X-2","12:90:889","H fks",563359987, 988989, true)
            )
        }


        LaunchedEffect(true) {
            beaconDevices.forEach {
                dao.upsert(it)
            }
        }

        println( "All the saved data after  $people")




        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Beacon Control Card
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth() // Makes width full screen
                    .wrapContentHeight() // Height adjusts based on content
                    .clip(RoundedCornerShape(18.dp)).clickable {
                        navigationState.navigateTo(Screen.DummyScreen)
                    }
                    .border(
                        width = 1.dp,
                        color = CardBorderColor,
                        shape = RoundedCornerShape(18.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface // Light gray background
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // No shadow
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
/*

                        Image(
                            painter = painterResource(Res.drawable.compose-multiplatform),
                            contentDescription = "My Image",
                            modifier = Modifier
                                .size(50.dp)

                            )
*/


                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Beacon",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }


                    Switch(
                        checked = isBeaconEnabled,
                        onCheckedChange = { isBeaconEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.secondary, // Blue thumb
                            checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), // Light blue track
                            uncheckedThumbColor = Color.Gray, // Gray thumb when unchecked
                            uncheckedTrackColor = Color.LightGray // Light gray track when unchecked
                        )
                    )
                }
            }

            if (isScanning) {
                Button(
                    onClick = { /* Handle connect */ },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                        contentColor = Color.White // White text
                    ),
                    modifier = Modifier.wrapContentWidth()
                ){
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
                    onClick = {  selectedType = "Eddy Stone" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == "Eddy Stone") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = Color.White // White text
                    ),
                    modifier = Modifier.weight(1f)
                ){
                    Text("Eddy Stone")
                }

                Button(
                    onClick = {  selectedType = "Beacon" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == "Beacon") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = Color.White // White text
                    ),
                    modifier = Modifier.weight(1f)
                ){
                    Text("Beacon")
                }

            }

            // Beacon List

            LazyColumn {
                items(beaconDevices) { device ->

                    BeaconItem(device)

                }
            }


        }
    }
}


@Composable
fun BeaconItem(beacon: BeaconDevice) {
    var isSaved by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
       colors = CardDefaults.cardColors(
           containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(12.dp)
    ) {
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
                    Button(

                        onClick = {
                            isSaved = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = "Save", color = Color.White)
                    }
                }
            }
        }
    }
}