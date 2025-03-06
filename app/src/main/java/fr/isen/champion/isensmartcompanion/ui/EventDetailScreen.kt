package fr.isen.champion.isensmartcompanion.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.isen.champion.isensmartcompanion.R
import fr.isen.champion.isensmartcompanion.data.AppDatabase
import fr.isen.champion.isensmartcompanion.data.entity.EventEntity
import fr.isen.champion.isensmartcompanion.helper.NotificationHelper.scheduleEventNotification
import fr.isen.champion.isensmartcompanion.ui.theme.IsensmartcompanionTheme
import kotlinx.coroutines.launch

class EventDetailScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.getParcelableExtra<EventEntity>("EVENT")
        setContent {
            IsensmartcompanionTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    EventDetailScreenContent(event)
                }
            }
        }
    }
}

@Composable
fun EventDetailScreenContent(event: EventEntity?) {
    val context = LocalContext.current
    if (event == null) {
        Text(stringResource(R.string.event_details_screen_label_no_event_data_available))
    } else {
        val db = AppDatabase.getDatabase(context)
        val pinnedEventDao = db.pinnedEventDao()
        val isPinnedCount by pinnedEventDao.isPinned(event.id).collectAsState(initial = 0)
        val isPinned = isPinnedCount > 0
        val scope = rememberCoroutineScope()

        fun togglePin() = scope.launch {
            if (!isPinned) {
                pinnedEventDao.insertPinnedEvent(event)
                Toast.makeText(context, R.string.event_details_screen_label_notification_send, Toast.LENGTH_SHORT).show()
                scheduleEventNotification(context, event, 5_000L)
            } else {
                pinnedEventDao.deleteByEventId(event.id)
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Text(event.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            Text(event.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(Modifier.height(16.dp))

            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.event_details_label_date) + event.date)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.event_details_label_location) + event.location)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.event_details_label_category) + event.category)

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val reminderText = stringResource(R.string.event_details_screen_label_reminder)
                Text(reminderText)
                Spacer(Modifier.width(8.dp))

                val iconColor = if (isPinned) MaterialTheme.colorScheme.primary else Color.Gray
                IconButton(onClick = { togglePin() }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = if (isPinned) {
                            stringResource(R.string.event_details_screen_label_disable_reminder)
                        } else {
                            stringResource(R.string.event_details_screen_label_enable_reminder)
                        },
                        tint = iconColor
                    )
                }
            }
        }
    }
}