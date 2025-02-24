package fr.isen.champion.isensmartcompanion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.champion.isensmartcompanion.ui.theme.IsensmartcompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IsensmartcompanionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AssistantUI(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantUI(modifier: Modifier = Modifier) {
    // State for the question typed by the user
    var question by remember { mutableStateOf("") }
    // State for the response displayed
    var response by remember { mutableStateOf("") }

    // We need the context to show a Toast
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFBF8FF) // Adjust this to match your desired background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Large red title
            Text(
                text = "ISEN",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 36.sp,
                    color = Color(0xFFC30000)   // Red color
                )
            )
            // Smaller subtitle
            Text(
                text = "Smart Companion",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            )

            // A little space between title/subtitle and the conversation
            Spacer(modifier = Modifier.height(16.dp))

            // Display the AI's "response" (conversation)
            Text(
                text = response,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            // Push the text field row to the bottom
            Spacer(modifier = Modifier.weight(1f))

            // Text field + send button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFE6E6F5),  // Light purple/gray background
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text field without an outline, just a transparent container
                TextField(
                    value = question,
                    onValueChange = { question = it },
                    placeholder = { Text("Ask a question") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Round send button with arrow icon
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                        // Update the response based on user input
                        response = "You asked: $question"
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC30000)) // Red circle
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssistantUIPreview() {
    IsensmartcompanionTheme {
        AssistantUI()
    }
}
