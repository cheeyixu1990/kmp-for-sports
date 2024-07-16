import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient

fun main() {
    val prefs = createDataStore {
        DATA_STORE_FILE_NAME
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KmpForFun",
        ) {
            App(
                prefs = prefs,
                client = remember {
                    InsultCensorClient(createHttpClient(OkHttp.create()))
                }
            )
        }
    }
}