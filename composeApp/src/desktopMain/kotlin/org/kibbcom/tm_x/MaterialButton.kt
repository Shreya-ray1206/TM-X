package org.kibbcom.tm_x

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
actual fun MaterialButton(text: String, onClick: () -> Unit) {
    androidx.compose.material3.Button(onClick = onClick) {
        Text(text)
    }
}
