package fr.isen.champion.isensmartcompanion.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import fr.isen.champion.isensmartcompanion.R
import fr.isen.champion.isensmartcompanion.data.AppDatabase
import fr.isen.champion.isensmartcompanion.data.entity.EventEntity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgendaScreen() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val pinnedEventDao = db.pinnedEventDao()

    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)
    }

    val pinnedEventsFlow = pinnedEventDao.getAllPinnedEvents()
    val pinnedEvents by pinnedEventsFlow.collectAsState(emptyList())

    val pinnedEventsByDate = pinnedEvents.groupBy { event ->
        val dateString = if (event.date.startsWith("1er ")) event.date.replaceFirst("1er ", "1 ")
        else event.date
        runCatching { LocalDate.parse(dateString, dateFormatter) }.getOrNull()
    }.filterKeys { it != null }.mapKeys { it.key!! }

    val currentMonth = YearMonth.now()
    val startMonth = currentMonth.minusYears(5)
    val endMonth = currentMonth.plusYears(5)
    val firstDayOfWeek = firstDayOfWeekFromLocale()

    val state = rememberCalendarState(
        startMonth, endMonth, currentMonth, firstDayOfWeek
    )

    val scope = rememberCoroutineScope()
    LaunchedEffect(pinnedEventsByDate) {
        pinnedEventsByDate.keys.minOrNull()?.let { earliest ->
            val target = YearMonth.from(earliest).coerceIn(startMonth, endMonth)
            scope.launch {
                kotlinx.coroutines.delay(200)
                state.scrollToMonth(target)
            }
        }
    }

    var selectedEvents by remember { mutableStateOf<List<EventEntity>>(emptyList()) }

    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(stringResource(R.string.agenda_screen_label_title), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.agenda_screen_label_description), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(Modifier.height(16.dp))

        HorizontalCalendar(
            Modifier.fillMaxWidth(),
            state = state,
            monthHeader = { month ->
                val monthYearText = month.yearMonth.format(
                    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)
                ).replaceFirstChar { it.uppercaseChar() }
                Text(monthYearText, Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.titleMedium)
            },
            dayContent = { day ->
                if (day.position == DayPosition.MonthDate) {
                    val eventsForDay = pinnedEventsByDate[day.date].orEmpty()
                    DayCell(day.date, eventsForDay) {
                        selectedEvents = eventsForDay.takeIf { it.isNotEmpty() } ?: emptyList()
                    }
                } else {
                    Spacer(Modifier.size(40.dp))
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        if (selectedEvents.isNotEmpty()) {
            Text(stringResource(R.string.agenda_screen_label_event_selected), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            selectedEvents.forEach { event ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text(stringResource(R.string.event_details_label_title) + event.title)
                        Text(stringResource(R.string.event_details_label_description) + event.description)
                        Text(stringResource(R.string.event_details_label_location) + event.location)
                    }
                }
            }
        } else {
            Text(stringResource(R.string.agenda_screen_label_no_event_selected))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    pinnedEvents: List<EventEntity>,
    onDayClick: () -> Unit
) {
    val hasPinned = pinnedEvents.isNotEmpty()
    val bgColor = if (hasPinned) Color.Red else Color.Transparent
    val textColor = if (hasPinned) Color.White else Color.Black

    Box(
        Modifier
            .size(48.dp)
            .border(1.dp, Color.LightGray)
            .background(bgColor)
            .padding(4.dp)
            .clickable { onDayClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(date.dayOfMonth.toString(), color = textColor)
    }
}
