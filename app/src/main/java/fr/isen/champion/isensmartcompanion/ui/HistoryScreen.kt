package fr.isen.champion.isensmartcompanion.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.isen.champion.isensmartcompanion.R
import fr.isen.champion.isensmartcompanion.data.AppDatabase
import fr.isen.champion.isensmartcompanion.data.entity.ConversationHistoryEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = AppDatabase.getDatabase(context).conversationHistoryDao()

    val conversationList by dao.getAllConversations().collectAsState(emptyList())

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.history_screen_label_title), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.history_screen_label_description), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(Modifier.height(16.dp))

        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(conversationList) { item ->
                HistoryItem(item) {
                    scope.launch { dao.deleteConversationById(item.id) }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { scope.launch { dao.clearHistory() } },
            Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.history_screen_label_clear_all))
        }
    }
}

@Composable
fun HistoryItem(history: ConversationHistoryEntity, onDelete: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val dateString = dateFormat.format(Date(history.timestamp))

    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(stringResource(R.string.history_screen_label_date) + dateString, style = MaterialTheme.typography.bodySmall)
            Text(stringResource(R.string.history_screen_label_question) + history.question, style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.history_screen_label_answer) + history.answer, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Button(onClick = onDelete) {
                Text(stringResource(R.string.history_screen_label_delete))
            }
        }
    }
}
