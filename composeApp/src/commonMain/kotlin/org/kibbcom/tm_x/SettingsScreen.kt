package org.kibbcom.tm_x

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CompletionHandler

@Composable
fun SettingsScreen() {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 40.dp)
    ) {
        item {
            DropDown(title = "Device Info", imageUrl = "https://cdn-icons-png.flaticon.com/512/411/411727.png") {
                DeviceInfo()
            }
        }

        item {
            DropDown(title = "Other Settings", imageUrl = "https://cdn-icons-png.freepik.com/256/900/900834.png?semt=ais_hybrid") {
                OtherInfo()
            }
        }

        item {
            ToggleSection(title = "LED Settings", imageUrl = "https://cdn-icons-png.freepik.com/256/6092/6092698.png?semt=ais_hybrid") {
                LedControl()
            }
        }

        item {
            DropDown(title = "Beacon Devices and Tracking", imageUrl = "https://w1.pngwing.com/pngs/788/838/png-transparent-family-symbol-ibeacon-bluetooth-low-energy-beacon-proximity-marketing-eddystone-altbeacon-apple-nearfield-communication-thumbnail.png") {
                BeaconDevices()
            }
        }

        item {
            DropDown(title = "Server Settings", imageUrl = "https://cdn-icons-png.flaticon.com/512/2092/2092780.png") {
                SeverSettings()
            }
        }

        item {
            ToggleSection(title = "Vibrator Settings", imageUrl = "https://cdn-icons-png.flaticon.com/512/733/733525.png") {
                VibratorSettings()
            }
        }
        item {
            ToggleSection(title = "Accelerometer Settings", imageUrl = "https://cdn-icons-png.flaticon.com/512/11256/11256223.png") {
                AccelerometerSettings()
            }
        }
        item {
            ToggleSection(title = "LTE Settings", imageUrl = "https://cdn-icons-png.flaticon.com/512/2313/2313502.png") {
                LteSettings()
            }
        }

        item {
            ToggleSection(title = "Light Sensor", imageUrl = "https://cdn-icons-png.flaticon.com/512/11298/11298689.png") {
                LightSensor()
            }
        }

        item {
            DropDown(title = "Audio Codec", imageUrl = "https://cdn-icons-png.flaticon.com/512/3871/3871560.png") {
                AudioCodec()
            }
        }

        item {
            DropDown(title = "Send GPS Fix", imageUrl = "https://cdn-icons-png.flaticon.com/512/3871/3871560.png") {
               SendGpsFix()
            }
        }
    }
}


@Composable
fun DropDown(
    title: String,
    imageUrl: String,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Dropdown Image",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier.size(24.dp)
            )
        }

        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                content()
            }
        }
    }
}


@Composable
fun ToggleSection(
    title: String,
    imageUrl: String,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Toggle Image",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Switch(
                checked = isExpanded,
                onCheckedChange = { isExpanded = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Green,
                    uncheckedThumbColor = Color.Gray
                )
            )
        }

        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                content()
            }
        }
    }
}


@Composable
fun DeviceInfo() {
    var deviceName by remember { mutableStateOf("") }
    var deviceAddress by remember { mutableStateOf("") }
    var deviceNo by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        EditableRow(value = deviceName, onValueChange = { deviceName = it }, label = "Device Name")
        EditableRow(value = deviceAddress, onValueChange = { deviceAddress = it }, label = "Device Address")
        EditableRow(value = deviceNo, onValueChange = { deviceNo = it }, label = "Device No")

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
           Button(
               onClick = {},
               colors = ButtonDefaults.buttonColors(
                   backgroundColor = Color(0xFF88D66C),
                   contentColor = Color.White
               )
           ){
             Text("Activate Device")
           }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF93827),
                    contentColor = Color.White
                )
            ){
                Text("Reset Setting")
            }
        }
    }
}

@Composable
fun EditableRow(value: String, onValueChange: (String) -> Unit, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically // Aligns icon and text field
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(label) },
            modifier = Modifier
                .weight(1f) // Allows space for the icon
                .padding(2.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp // Adjust the font size of the text
            )
        )

        Spacer(modifier = Modifier.width(8.dp)) // Adds spacing before the icon

        IconButton(onClick = { /* Handle click */ }) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
    }
}


@Composable
fun BeaconDevices() {
    val beaconDevices = remember {
        listOf(
            BeaconDevice("Sony JBL","12:90:889","Bsi23",563359987, 988989, true),
            BeaconDevice("TM-X","12:90:889","Bsi23",563359987, 988989,false),
            BeaconDevice("Sony JBL","12:90:889","Bsi23",563359987, 988989, true),
            BeaconDevice("TM-X","12:90:889","Bsi23",563359987, 988989, true)
        )
    }

    // Beacon List
    Column(modifier = Modifier.fillMaxSize().padding(vertical = 8.dp)) {
        beaconDevices.forEach { beacon ->
            BeaconItem(beacon = beacon)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SeverSettings () {
    var mqttUrl by remember { mutableStateOf("") }
    var portNo by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)


    ) {
        EditableRow(value = mqttUrl, onValueChange = { mqttUrl = it }, label = "MQTT Url")
        EditableRow(value = portNo, onValueChange = { portNo = it }, label = "Port Number")
        EditableRow(value = userName, onValueChange = { userName= it }, label = "Username")
        EditableRow(value = password, onValueChange = { password= it }, label = "Password")

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF93827),
                contentColor = Color.White
            )
        ){
            Text("Update Info")
        }
    }
}

@Composable
fun OtherInfo() {
    var timeOfGPS by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Add spacing between items
    ) {
        EditableRow(value = timeOfGPS , onValueChange = { timeOfGPS = it }, "GPS Time Interval (IN SECS")

       Row() {
           Text("Select Language")

           Icon(
               imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
               contentDescription = "Dropdown for the led color change"
           )
       }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF88D66C),
                contentColor = Color.White
            )
        ) {
            Text("Update Other Info")
        }
    }
}

@Composable
fun LedControl() {
    val isExpanded by remember { mutableStateOf(false) }
  Column (
      modifier = Modifier
          .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp) // Add spacing between items
  ){
      Row(
          modifier = Modifier
              .fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
      ) {
          Text(
              text = "Alarm Led Color",
          fontSize = 14.sp)

          Row() {
              Text("Red")

              Icon(
                  imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                  contentDescription = "Dropdown for the led color change"
              )

              if(isExpanded) {
//                  Then the logic of color change or somethings else goes here
              }
          }
      }

      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
      ) {
          Text(text = "LED Brightness Controller")
      }

      Row(
          modifier = Modifier
              .fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
      ) {
          Column (
              verticalArrangement = Arrangement.spacedBy(8.dp),
          ){
              Row(
                  horizontalArrangement = Arrangement.Center,

              ) {
                  Text(text ="Red")
              }

              Row(
                  horizontalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                  Button(
                      onClick = {},
                      colors = ButtonDefaults.buttonColors(
                          backgroundColor = Color(0xFFF93827), // Red for Decrease
                          contentColor = Color.White
                      )
                  ) {
                      Text(text = "-")
                  }

                  Button(
                      onClick = {},
                      colors = ButtonDefaults.buttonColors(
                          backgroundColor = Color(0xFF88D66C), // Green for Increase
                          contentColor = Color.White
                      )
                  ) {
                      Text(text = "+")
                  }
              }
          }

          Column (
              verticalArrangement = Arrangement.spacedBy(8.dp)
          ){
              Row(
                  horizontalArrangement = Arrangement.End
              ) {
                  Text(text ="Green")
              }

              Row(
                  horizontalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                  Button(
                      onClick = {},
                      colors = ButtonDefaults.buttonColors(
                          backgroundColor = Color(0xFFF93827), // Red for Decrease
                          contentColor = Color.White
                      )
                  ) {
                      Text(text = "-")
                  }

                  Button(
                      onClick = {},
                      colors = ButtonDefaults.buttonColors(
                          backgroundColor = Color(0xFF88D66C), // Green for Increase
                          contentColor = Color.White
                      )
                  ) {
                      Text(text = "+")
                  }
              }
          }
      }

  }
}

@Composable
fun VibratorSettings(){
    val isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Add spacing between items
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mode",
                fontSize = 14.sp)

            Row() {
                Text("Slow")

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Dropdown choose for mode of vibration"
                )

                if(isExpanded) {
//                  The logic of changing the mode goes here
                }
            }
        }

    }
}


@Composable
fun AccelerometerSettings() {
    var selectedRange by remember { mutableStateOf("Select") }
    var selectedResolution by remember { mutableStateOf("Select") }
    var selectedWakeGain by remember { mutableStateOf("Select") }
    var selectedSniffGain by remember { mutableStateOf("Select") }

    val rangeOptions = listOf("2G", "4G", "8G", "16G")
    val resolutionOptions = listOf("6-bit", "7-bit", "8-bit", "10-bit","11-bit","12-bit")
    val wakeGainOptions = listOf("1x", "2x", "4x", "8x")
    val sniffGainOptions = listOf("1x", "2x", "3x", "4x")

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DropdownSelector(title = "Set Range", options = rangeOptions, selectedOption = selectedRange) { selectedRange = it }
        DropdownSelector(title = "Set Resolution", options = resolutionOptions, selectedOption = selectedResolution) { selectedResolution = it }
        DropdownSelector(title = "Set Wake Gain", options = wakeGainOptions, selectedOption = selectedWakeGain) { selectedWakeGain = it }
        DropdownSelector(title = "Set Sniff Gain", options = sniffGainOptions, selectedOption = selectedSniffGain) { selectedSniffGain = it }
    }
}


@Composable
fun LteSettings() {
    val isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Select Network Mode",
                fontSize = 14.sp)

            Row() {
                Text("GSM")

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Dropdown choose for network of LTE"
                )

                if(isExpanded) {
//                  The logic of changing the mode goes here
                }
            }
        }

    }
}

@Composable
fun LightSensor() {
    var selectedMode by remember { mutableStateOf("Select") }

    val modesOptions = listOf("Continuous", "Device Id", "Default Value","Manufacture Id", "One Short")
    Column (
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        DropdownSelector(title = "Asle Modes", options = modesOptions, selectedOption = selectedMode) { selectedMode= it }

    }
}

@Composable
fun AudioCodec() {
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF88D66C),
                    contentColor = Color.White
                )
            ){
                Text("Initialize")
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF88D66C),
                    contentColor = Color.White
                )
            ){
                Text("Enable Tone Generator")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Gain(Vol)",
                fontSize = 14.sp,
                modifier = Modifier.weight(1f))


            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/4194/4194908.png",
                    contentDescription = "Toggle Image",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )

                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/4194/4194914.png",
                    contentDescription = "Toggle Image",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun DropdownSelector(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedOption)

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown Icon"
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        modifier = Modifier.background(Color.Black)
                    ) {
                        Text(text = option)
                    }
                }
            }
        }
    }
}

@Composable
fun SendGpsFix() {
    Text(text = "GPS Fix Sent", fontSize = 16.sp, fontWeight = FontWeight.Medium)
}
