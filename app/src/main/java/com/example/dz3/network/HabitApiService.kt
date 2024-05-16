package com.example.dz3.network

import com.example.dz3.models.HabitDone
import com.example.dz3.models.HabitRemote
import com.example.dz3.models.HabitUID
import retrofit2.Call
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
