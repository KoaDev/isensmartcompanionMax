package fr.isen.champion.isensmartcompanion.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.champion.isensmartcompanion.R
import fr.isen.champion.isensmartcompanion.data.AppDatabase
import fr.isen.champion.isensmartcompanion.data.entity.ConversationHistoryEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = AppDatabase.getDatabase(context)
    val dao = db.conversationHistoryDao()

    var question by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }

    val generativeModel = remember {
        com.google.ai.client.generativeai.GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyCMQRYDu4hELGQZqM6vD1IdBu_2JBLlcgw"
        )
    }

    Surface(Modifier.fillMaxSize(), color = Color(0xFFFBF8FF)) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 40.dp)
            ) {
                Text(
                    "ISEN",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 36.sp,
                        color = Color(0xFFC30000)
                    )
                )
                Text(
                    "Smart Companion",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(response, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE6E6F5), RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = question,
                        onValueChange = { question = it },
                        placeholder = { Text(stringResource(R.string.assistant_screen_label_asking)) },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            Toast.makeText(context, R.string.assistant_screen_label_toast_success, Toast.LENGTH_SHORT).show()
                            response = ""
                            scope.launch {
                                try {
                                    generativeModel.generateContentStream(question).collect { chunk ->
                                        response += chunk.text
                                    }
                                    dao.insertConversation(
                                        ConversationHistoryEntity(
                                            question = question,
                                            answer = response,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                } catch (e: Exception) {
                                    Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFC30000))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = stringResource(R.string.assistant_screen_label_send),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
