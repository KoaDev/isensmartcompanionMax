package fr.isen.champion.isensmartcompanion.data.entity;

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize //allow to transport data, to an Activity to another one without override the entity
@Entity(tableName = "pinned_events")
data class EventEntity(
        @PrimaryKey(autoGenerate = true) val pinnedId: Int = 0,
        val id: String,
        val title: String,
        val description: String,
        val date: String,
        val location: String,
        val category: String
) : Parcelable
