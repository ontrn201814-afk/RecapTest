package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.RecapViewModel
import com.example.ui.screens.MainScreen
import com.example.ui.theme.ZippyBg
import com.example.ui.theme.ZippyRecapTheme

class MainActivity : ComponentActivity() {
    private val viewModel: RecapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZippyRecapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ZippyBg
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
