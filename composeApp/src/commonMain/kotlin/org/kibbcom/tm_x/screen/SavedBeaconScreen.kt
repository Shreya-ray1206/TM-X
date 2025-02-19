package org.kibbcom.tm_x.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.db.AppDatabase
import org.kibbcom.tm_x.models.BeaconDevice
import org.kibbcom.tm_x.platform.BackHandler
import org.kibbcom.tm_x.platform.viewmoel_factory.BeaconViewModelFactory
import org.kibbcom.tm_x.theme.getToolbarAdditionColor
import org.kibbcom.tm_x.viewmodel.BeaconViewModel
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.beacon

@Composable
fun SavedBeaconScreen(db: AppDatabase, navigationState: NavigationNewState, paddingValues: PaddingValues,
                      viewModel: BeaconViewModel = viewModel(factory = BeaconViewModelFactory(db))
) {

    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = getCommonModifierForAdditionToolbar(80.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(getToolbarAdditionColor())
        ) {

            Box(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically // Ensures vertical alignment of Image and Text
                ){

                    Image(
                        painter = painterResource(Res.drawable.beacon), // Replace with your image resource
                        contentDescription = "Scanning Icon",
                        modifier = Modifier
                            .size(50.dp).padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.White) // Apply white tint (optional)
                    )

                    Text(
                        text = "This data is stored on your device. If you no longer need it, you can remove it.",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                }

            }

        }


        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }



        val savedBeacons by viewModel.savedBeacons.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(savedBeacons) { device ->

                BeaconSavedItem(device,viewModel)

            }
        }
    }


}

@Composable
fun BeaconSavedItem(beacon: BeaconDevice, viewModel: BeaconViewModel) {



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
                    Button(
                        onClick = {
                            viewModel.removeBeacon(beacon)

                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
                        ),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text =  "Remove" ,
                            color =  Color.White
                        )
                    }

                }
            }
        }
    }


}

