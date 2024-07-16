package com.yx.kmpforfun

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import createDataStore
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                prefs = remember {
                    createDataStore(applicationContext)
                },
                client = remember {
                    InsultCensorClient(createHttpClient(OkHttp.create()))
                }
            )
        }
    }
}
