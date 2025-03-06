package fr.isen.champion.isensmartcompanion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.isen.champion.isensmartcompanion.data.dao.ConversationHistoryDao
import fr.isen.champion.isensmartcompanion.data.dao.PinnedEventDao
import fr.isen.champion.isensmartcompanion.data.entity.ConversationHistoryEntity
import fr.isen.champion.isensmartcompanion.data.entity.EventEntity

@Database(
    entities = [ConversationHistoryEntity::class, EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun conversationHistoryDao(): ConversationHistoryDao

    abstract fun pinnedEventDao(): PinnedEventDao

    companion object { //same think of static things in Java
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
