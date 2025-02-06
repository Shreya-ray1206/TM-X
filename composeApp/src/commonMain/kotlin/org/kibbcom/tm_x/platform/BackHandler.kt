package org.kibbcom.tm_x.platform

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(onBack: () -> Unit)