package com.example.data.network

import com.example.domain.models.HabitDone
import com.example.domain.models.HabitRemote
import com.example.domain.models.HabitUID
import retrofit2.Response
import retrofit2.http.*


interface HabitApiService {

    @GET("habit")
    suspend fun getHabits(): Response<List<HabitRemote>>

    @PUT("habit")
    suspend fun putHabit(@Body habit: HabitRemote): Response<HabitUID>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body uid: HabitUID): Response<Void>

    @POST("habit_done")
    suspend fun markHabitDone(@Body habitDone: HabitDone): Response<HabitUID>
}
