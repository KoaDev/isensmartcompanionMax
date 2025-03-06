package fr.isen.champion.isensmartcompanion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.isen.champion.isensmartcompanion.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PinnedEventDao {

    @Insert
    suspend fun insertPinnedEvent(eventEntity: EventEntity): Long

    @Query("DELETE FROM pinned_events WHERE id = :id")
    suspend fun deleteByEventId(id: String): Int

    // Check if an event is pinned
    @Query("SELECT COUNT(*) FROM pinned_events WHERE id = :id")
    fun isPinned(id: String): Flow<Int>

    // If you want to retrieve all pinned events
    @Query("SELECT * FROM pinned_events")
    fun getAllPinnedEvents(): Flow<List<EventEntity>>
}
