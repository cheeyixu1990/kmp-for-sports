import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import io.ktor.client.engine.darwin.Darwin
import networking.InsultCensorClient
import networking.createHttpClient


fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App(
        prefs = remember {
            createDataStore()
        },
        client = remember {
            InsultCensorClient(createHttpClient(Darwin.create()))
        }
    )
}