package org.kibbcom.tm_x

import androidx.compose.runtime.Composable

@Composable
expect fun MaterialButton(text: String, onClick: () -> Unit)
