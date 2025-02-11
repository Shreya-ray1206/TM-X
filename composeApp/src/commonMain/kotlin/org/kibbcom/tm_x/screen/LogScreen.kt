package org.kibbcom.tm_x

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun LogScreen(navigationState: NavigationNewState, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues)
    ) {
        // Top Section: RecyclerView and TextView
        LogListSection()

        // Bottom Section: Buttons and Spinner
        BottomActionSection()
    }
}

// Data class to represent a log entry
data class LogEntry(
    val id: Int,
    val message: String,
    val date: String
)

@Composable
fun LogListSection(

) {
    val logs = remember { mutableStateListOf<Any>(
        LogEntry(id = 1, message = "Hello World", date = "12th June"),
        LogEntry(id = 2, message = "Another log", date = "13th June"),
        LogEntry(id = 3, message = "Yet another log", date = "14th June")
    ) }
    val noLogsVisible = logs.isEmpty()

    Column(
        modifier = Modifier
            .padding(10.dp, 0.dp)
    ) {
        if (logs.isEmpty()) {
            Text("No logs available")
        } else {
            logs.forEach { log ->
                LogItem(log = log)
            }
        }
    }
}

@Composable
fun LogItem(log: Any) {

    // Safely cast `log` to `LogEntry`
    if (log is LogEntry) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                ,
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
    } else {

        Text("Invalid log format")
    }
}

@Composable
fun BottomActionSection() {
    val spinnerOptions = listOf("Option 1", "Option 2", "Option 3")
    var selectedSpinnerOption by remember { mutableStateOf(spinnerOptions[0]) }
    var expanded by remember { mutableStateOf(false) }

    // Row containing the dropdown and icons
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