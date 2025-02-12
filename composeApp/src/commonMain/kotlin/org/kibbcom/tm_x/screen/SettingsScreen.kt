package org.kibbcom.tm_x.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.getImageResourcePath
import org.kibbcom.tm_x.models.BeaconDevice
import org.kibbcom.tm_x.platform.BackHandler
import tm_x.composeapp.generated.resources.Accelerometer_settings
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.beacon
import tm_x.composeapp.generated.resources.device_info
import tm_x.composeapp.generated.resources.led
import tm_x.composeapp.generated.resources.light_sensor
import tm_x.composeapp.generated.resources.lte_settings
import tm_x.composeapp.generated.resources.other_setting
import tm_x.composeapp.generated.resources.sever_settings
import tm_x.composeapp.generated.resources.vibrator_settings
import tm_x.composeapp.generated.resources.vol_settings


@Composable
fun SettingsScreen(navigationState: NavigationNewState, paddingValues : PaddingValues,

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background), // Uses M3 background color
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }


        LazyColumn (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            item {
                DropDown(
                    title = "Device Info",
                    image = painterResource(Res.drawable.device_info)
                ) {
                    DeviceInfo()

                }
            }

            item {
                DropDown(
                    title = "Other Settings",
                    image = painterResource(Res.drawable.other_setting)
                ) {
                    OtherInfo()
                }
            }

            item {
                ToggleSection(
                    title = "LED Settings",
                    image = painterResource(Res.drawable.led)
                ) {
                    LedControl()
                }
            }

            item {
                DropDown(title = "Beacon Devices and Tracking",
                    painterResource(Res.drawable.beacon)
                ) {
                    //todo need fix with material 3
                 BeaconDevices()
                }
            }

            item {
                DropDown(
                    title = "Server Settings",
                    painterResource(Res.drawable.sever_settings)
                ) {
                    SeverSettings()
                }
            }

            item {
                ToggleSection(
                    title = "Vibrator Settings",
                    painterResource(Res.drawable.vibrator_settings)
                ) {
                    VibratorSettings()
                }
            }
            item {
                ToggleSection(
                    title = "Accelerometer Settings",
                    painterResource(Res.drawable.Accelerometer_settings)) {
                    AccelerometerSettings()
                }
            }
            item {
                ToggleSection(
                    title = "LTE Settings",
                    painterResource(Res.drawable.lte_settings)
                ) {
                    LteSettings()
                }
            }

            item {
                ToggleSection(
                    title = "Light Sensor",
                    painterResource(Res.drawable.light_sensor)
                ) {
                    LightSensor()
                }
            }

            item {
                DropDown(
                    title = "Audio Codec",
                    painterResource(Res.drawable.vol_settings)
                ) {
                    AudioCodec()
                }
            }

            item {
                DropDown(
                    title = "Send GPS Fix",
                    painterResource(Res.drawable.vol_settings) ) {
                    SendGpsFix()
                }
            }
        }
    }




}


@Composable
fun DropDown(
    title: String,
    image :Painter,
    content: @Composable () -> Unit,

) {
    var isExpanded by remember { mutableStateOf(false) }

    // Get the platform-specific image resource path
//    val image = getImageResourcePath(imageName)



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
        ,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded } ,// I haven't removed here
            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                AsyncImage(
//                    model = painterResource(Res.drawable.device_info),
//                    contentDescription = "Dropdown Image",
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .size(35.dp),
//                    contentScale = ContentScale.Crop
//                )

                Image(
                    painter = image,  // Correct way to use it
                    contentDescription = "Dropdown Image",
                    modifier = Modifier
                        .wrapContentSize()
                        .size(50.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )

//                Spacer(modifier = Modifier.width(8.dp))

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

            ) {
                content()
            }
        }
    }
}


@Composable
fun ToggleSection(
    title: String,
    image: Painter,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                AsyncImage(
//                    model = imageUrl,
//                    contentDescription = "Toggle Image",
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .size(35.dp),
//                    contentScale = ContentScale.Crop
//                )

                Image(
                    painter = image,  // Correct way to use it
                    contentDescription = "Dropdown Image",
                    modifier = Modifier
                        .wrapContentSize()
                        .size(50.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )

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
                    .fillMaxWidth(),
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

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
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
                    containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                    contentColor = Color.White // White text
                )
            ){
                Text("Activate Device")
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                    contentColor = Color.White // White text
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
            .fillMaxHeight()
            .padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically, // Aligns icon and text field
        horizontalArrangement = Arrangement.SpaceBetween
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

//        Spacer(modifier = Modifier.width(8.dp)) // Adds spacing before the icon

        IconButton(onClick = { /* Handle click */ }) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
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
                containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                contentColor = Color.White // White text
            )
        ){
            Text("Update Info")
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = 10.dp)) {
        beaconDevices.forEach { beacon ->
            BeaconItem(beacon = beacon)

        }
    }
}

@Composable
fun OtherInfo() {
    var timeOfGPS by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp),
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
                containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                contentColor = Color.White // White text
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
            .padding(horizontal = 5.dp, vertical = 10.dp),
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
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
                        )
                    ) {
                        Text(text = "-")
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
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
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
                        )
                    ) {
                        Text(text = "-")
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
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
            .padding(horizontal = 5.dp, vertical = 10.dp),
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
            .padding(horizontal = 5.dp, vertical = 10.dp),
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
            .padding(horizontal = 5.dp, vertical = 10.dp),
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
            .padding(horizontal = 5.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        DropdownSelector(title = "Asle Modes", options = modesOptions, selectedOption = selectedMode) { selectedMode= it }

    }
}

@Composable
fun AudioCodec() {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp),
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
                    containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                    contentColor = Color.White // White text
                )
            ){
                Text("Initialize")
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                    contentColor = Color.White // White text
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
                        text = {
                            Text(
                                text = option,
                                color = MaterialTheme.colorScheme.onSurface // Use M3 onSurface color for text
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SendGpsFix() {
    Text(text = "GPS Fix Sent", fontSize = 16.sp, fontWeight = FontWeight.Medium)
}





