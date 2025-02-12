package org.kibbcom.tm_x

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextUtils.initialize(applicationContext)

        setContent {
            App(navigationState = remember { NavigationNewState() })
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}