package fr.isen.champion.isensmartcompanion.data.api

import fr.isen.champion.isensmartcompanion.data.entity.EventEntity
import retrofit2.http.GET

interface EventsApi {
    @GET("events.json")
    suspend fun getEvents(): List<EventEntity>
}