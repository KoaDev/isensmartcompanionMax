package fr.isen.champion.isensmartcompanion.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.isen.champion.isensmartcompanion.R
import fr.isen.champion.isensmartcompanion.data.api.EventsApi
import fr.isen.champion.isensmartcompanion.data.entity.EventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var events by remember { mutableStateOf<List<EventEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(EventsApi::class.java)
            // On récupère un Map et on convertit ses valeurs en List
            events = withContext(Dispatchers.IO) { service.getEvents() }
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }


    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(stringResource(R.string.event_details_screen_label_title), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.event_details_screen_label_description), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(events) { event ->
                    EventItem(event) {
                        context.startActivity(
                            Intent(context, EventDetailScreen::class.java).apply {
                                putExtra("EVENT", event)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(event: EventEntity, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(event.title)
        Text(event.date)
    }
}
