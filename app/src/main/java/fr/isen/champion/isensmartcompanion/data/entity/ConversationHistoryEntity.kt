package fr.isen.champion.isensmartcompanion.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversation_history")
data class ConversationHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val timestamp: Long
)
