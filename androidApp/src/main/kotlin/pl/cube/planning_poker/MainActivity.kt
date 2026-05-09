package pl.cube.planning_poker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pl.cube.planning_poker.preferences.initializeUserPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUserPreferences(this)

        setContent {
            App()
        }
    }
}
