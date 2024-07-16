import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpforfun.composeapp.generated.resources.Res
import kmpforfun.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import networking.InsultCensorClient
import util.NetworkError
import util.onError
import util.onSuccess

@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>,
    client: InsultCensorClient
) {
    val counter by prefs
        .data
        .map {
            val counterKey = intPreferencesKey("counter")
            it[counterKey] ?: 0
        }
        .collectAsState(0)
    val scope = rememberCoroutineScope()
    MaterialTheme {
        var censoredText by remember {
            mutableStateOf<String?>(null)
        }
        var uncensoredText by remember {
            mutableStateOf("")
        }
        var isLoading by remember {
            mutableStateOf(false)
        }
        var errorMessage by remember {
            mutableStateOf<NetworkError?>(null)
        }

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = counter.toString(),
                textAlign = TextAlign.Center,
                fontSize = 50.sp
            )
            Button(onClick = {
                scope.launch {
                    prefs.edit { datastore ->
                        val counterKey = intPreferencesKey("counter")
                        datastore[counterKey] = counter + 1
                    }
                }
            }) {
                Text("Increment")
            }


            TextField(
                value = uncensoredText,
                onValueChange = { uncensoredText = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text("Uncensored text")
                }
            )

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null

                        client.censorWords(uncensoredText)
                            .onSuccess {
                                censoredText = it
                            }
                            .onError {
                                errorMessage = it
                            }
                        isLoading = false
                    }
                }) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(15.dp),
                        strokeWidth = 1.dp,
                        color = Color.White
                    )
                } else {
                    Text("Censor!")
                }
            }

            censoredText?.let {
                Text(it)
            }
            errorMessage?.let {
                Text(
                    it.name
                )
            }
        }
    }
}