package com.galih.noteappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.galih.noteappcompose.ui.screen.NavGraphs
import com.galih.noteappcompose.ui.theme.NoteAppComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppComposeTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}