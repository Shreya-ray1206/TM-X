package org.kibbcom.tm_x

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.kibbcom.tm_x.common.getToolbarAdditionColor
import org.kibbcom.tm_x.platform.BackHandler
import org.kibbcom.tm_x.platform.PlatformUtils
import org.kibbcom.tm_x.screen.DeviceItem
import org.kibbcom.tm_x.screen.getCommonModifierForAdditionToolbar

@Composable
fun LogScreen(navigationState: NavigationNewState, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Toolbar Section
            Column(
                modifier = getCommonModifierForAdditionToolbar(40.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(getToolbarAdditionColor())
            ){

            }

            BackHandler {
                navigationState.navigateBack()  // Handle back press
            }

            val logs2 = listOf(
                LogEntry(1, "App started", "2025-02-24"),
                LogEntry(2, "User logged in", "2025-02-24"),
                LogEntry(3, "Error: Uncaught Error", "2025-02-24"),
                LogEntry(4, "Error: logged issue", "2025-02-24"),
                LogEntry(5, "Error: Permission issue", "2025-02-24"),
                LogEntry(6, "Error: logged issue", "2025-02-24"),
                LogEntry(7, "Error: Permission issue", "2025-02-24"),
                LogEntry(8, "Error: Permission issue", "2025-02-24"),
                LogEntry(9, "Error: Permission issue", "2025-02-24"),
                LogEntry(10, "Error: Permission issue", "2025-02-24"),
            )

            // Scrollable LazyColumn (Takes Remaining Space)
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Ensures it takes available space and scrolls
                    .fillMaxWidth() ,
                        contentPadding = PaddingValues(bottom = 60.dp) // Prevents last item from hiding

            ) {
                items(logs2) { logEntry ->
                    LogItem(logEntry)
                }
            }
        }

        // Fix BottomActionSection1 at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Gray)
        ) {
            BottomActionSection()
        }
    }
}




@Composable
fun BottomActionSection() {
    val spinnerOptions = listOf("Option 1", "Option 2", "Option 3")
    var selectedSpinnerOption by remember { mutableStateOf(spinnerOptions[0]) }
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Dropdown menu
        Row {
            // Display the selected option and dropdown arrow
            Row(

                modifier = Modifier.clickable{ expanded = true }
            ) {
                Text(
                    text = selectedSpinnerOption,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow"
                )
            }

            // Dropdown menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                spinnerOptions.forEach { option ->
//                    DropdownMenuItem(
//                        onClick = {
//                            selectedSpinnerOption = option
//                            expanded = false
//                        }
//                    ) {
//                        Text(text = option)
//                    }
                }
            }
        }
        // Row of icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp))
        {

            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/512/560/560512.png",
                contentDescription = "Toggle Image",
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Crop
            )

            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/512/560/560512.png",
                contentDescription = "Toggle Image",
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Crop
            )

            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/512/560/560512.png",
                contentDescription = "Toggle Image",
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Crop
            )

            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/512/560/560512.png",
                contentDescription = "Toggle Image",
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

data class LogEntry(
    val id: Int,
    val message: String,
    val date: String
)



@Composable
fun LogItem(log: LogEntry) {

    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("ID: ${log.id}")

            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Text("Date: ${log.date}")
        }
        Text("Message: ${log.message}")
    }
}