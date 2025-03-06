package fr.isen.champion.isensmartcompanion

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.isen.champion.isensmartcompanion.navigation.NavigationBar
import fr.isen.champion.isensmartcompanion.ui.theme.IsensmartcompanionTheme
import android.Manifest

// companinon object
// gradle
// pourquoi importznt le keystore
// dependance gradle (kts)
// pourquoi certeine chose et pas d'autre chose
// lazy column ? (différence lazycolumn / column)
// explication it ?


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 123)
            }
        }

        setContent {
            IsensmartcompanionTheme {
                NavigationBar()
            }
        }
    }
}
