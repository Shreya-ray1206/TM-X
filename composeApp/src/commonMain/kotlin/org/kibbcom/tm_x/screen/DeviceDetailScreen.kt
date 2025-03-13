package org.kibbcom.tm_x.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.common.component.rememberLineComponent
import com.patrykandpatrick.vico.multiplatform.common.fill


import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.common.getCommonCardColor
import org.kibbcom.tm_x.common.getToolbarAdditionColor
import org.kibbcom.tm_x.platform.BackHandler
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.accelerometer
import tm_x.composeapp.generated.resources.accelerometer_status
import tm_x.composeapp.generated.resources.bluetooth
import tm_x.composeapp.generated.resources.date_time
import tm_x.composeapp.generated.resources.device_information
import tm_x.composeapp.generated.resources.firmware_version
import tm_x.composeapp.generated.resources.ic_arrow_down
import tm_x.composeapp.generated.resources.ic_arrow_up
import tm_x.composeapp.generated.resources.manufacture
import tm_x.composeapp.generated.resources.model_number
import tm_x.composeapp.generated.resources.serial_number
import tm_x.composeapp.generated.resources.software_version


@Composable
fun DeviceDetailScreen(navigationState: NavigationNewState, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = getCommonModifierForAdditionToolbar(40.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(getToolbarAdditionColor())
        ) {
        }

        DeviceInfoCard()

        DeviceAccelerometerCard()

        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

    }
}
@Composable
fun DeviceInfoCard() {
    var isExpanded by remember { mutableStateOf(true) } // State to track expansion

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = getCommonCardColor())
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded } // Toggle expansion
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconTint = if (isSystemInDarkTheme()) Color.White else Color.Black

                Image(
                    painter = painterResource(Res.drawable.bluetooth),
                    contentDescription = "Bluetooth Icon",
                    modifier = Modifier.size(40.dp).padding(8.dp),
                    colorFilter =  ColorFilter.tint(iconTint)
                )

                Text(
                    text = stringResource(Res.string.device_information),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                Image(
                    painter = if (isExpanded) painterResource(Res.drawable.ic_arrow_up) else painterResource(Res.drawable.ic_arrow_down),
                    contentDescription = "Beacon Icon",
                    modifier = Modifier.size(20.dp).padding(2.dp),
                    colorFilter =  ColorFilter.tint(iconTint)
                )
            }

            // Expandable content
            if (isExpanded) {
                Column() {
                    Divider(color = Color.Gray, thickness = 1.dp)
                    InfoRow(stringResource(Res.string.manufacture), "tmx-1234-xyz")
                    InfoRow(stringResource(Res.string.model_number), "tmx-1234-xyz")
                    InfoRow(stringResource(Res.string.firmware_version), "fmv-123")
                    InfoRow(stringResource(Res.string.software_version), "soft-123")
                    InfoRow(stringResource(Res.string.serial_number), "TMX-123")
                    InfoRow(stringResource(Res.string.date_time), "25th Feb 2025 16:40")
                }
            }
        }
    }
}


@Composable
fun DeviceAccelerometerCard() {
    var isExpanded by remember { mutableStateOf(false) } // State to track expansion

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = getCommonCardColor())
    ) {
        Column {
            val iconTint = if (isSystemInDarkTheme()) Color.White else Color.Black

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded } // Toggle expansion
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.accelerometer),
                    contentDescription = "Bluetooth Icon",
                    modifier = Modifier.size(40.dp).padding(8.dp),
                    colorFilter =  ColorFilter.tint(iconTint)
                )

                Text(
                    text = stringResource(Res.string.accelerometer_status),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                Image(
                    painter = if (isExpanded) painterResource(Res.drawable.ic_arrow_up) else painterResource(Res.drawable.ic_arrow_down),
                    contentDescription = "Beacon Icon",
                    modifier = Modifier.size(20.dp).padding(2.dp),
                    colorFilter =  ColorFilter.tint(iconTint)
                )
            }

            // Expandable content
            if (isExpanded) {
                Column() {
                    Divider(color = Color.Gray, thickness = 1.dp)
                    ComposeMultiplatformBasicLineChart()

                    Spacer(Modifier.height((10.dp)))

                    ComposeMultiplatformBasicColumnChart()
                    Spacer(Modifier.height((10.dp)))
                    ComposeMultiplatformBasicComboChart()
                }
            }
        }
    }
}


// Helper function for displaying info rows
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Add padding for better spacing
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f) // Pushes the second text to the right
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End, // Aligns text to the right
            modifier = Modifier.fillMaxWidth(0.3f) // Ensures it stays at the right
        )
    }
}


@Composable
fun ComposeMultiplatformBasicLineChart() {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries { series(13, 8, 7, 12, 0, 1, 15, 14, 0, 11, 6, 12, 0, 11, 12, 11) }
        }
    }
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(),
            ),
        modelProducer = modelProducer,

        )
}

@Composable
fun ComposeMultiplatformBasicColumnChart() {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(5, 6, 5, 2, 11, 8, 5, 2, 15, 11, 8, 13, 12, 10, 2, 7) }
        }
    }
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(),
            ),
        modelProducer = modelProducer,
    )
}

@Composable
fun ComposeMultiplatformBasicComboChart() {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(4, 15, 5, 8, 10, 15, 9, 10, 7, 9, 10, 12, 2, 9, 5, 14) }
            lineSeries { series(1, 5, 4, 7, 3, 14, 5, 9, 9, 14, 7, 13, 14, 4, 10, 12) }
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(fill = fill(Color(0xffffc002)), thickness = 16.dp)
                )
            ),
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.Line(LineCartesianLayer.LineFill.single(fill(Color(0xffee2b2b))))
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
        ),
        modelProducer,

    )
}