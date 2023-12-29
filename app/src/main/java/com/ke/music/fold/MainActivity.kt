package com.ke.music.fold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.ke.music.fold.ui.components.LocalNavigationHandler
import com.ke.music.fold.ui.theme.FoldTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoldTheme {
                val windowSize = calculateWindowSizeClass(this)
                val navigationController = rememberNavController()

                CompositionLocalProvider(LocalNavigationHandler provides {
                    navigationController.navigate(it.createPath())
                }) {
                    MyApp(windowSize, navigationController)

                }
            }
        }
    }
}

