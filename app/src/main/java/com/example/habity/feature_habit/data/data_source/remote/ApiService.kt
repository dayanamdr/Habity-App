package com.example.habity.feature_habit.data.data_source.remote

import com.example.habity.feature_habit.domain.model.Habit
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/habit/{id}")
    suspend fun retrieveHabit(@Path("id") id: Int) : Response<Habit>

    @GET("/allHabits")
    suspend fun retrieveAllHabits() : Response<List<Habit>>

    @DELETE("/habit/{id}")
    suspend fun deleteHabit(@Path("id") id: Int?) : Response<Habit>

    @POST("/habit")
    suspend fun createHabit(@Body habit: Habit) : Response<Habit>

    @PUT("/habit/{id}")
    suspend fun updateHabit(@Path("id") id: Int, @Body habit: Habit) : Response<Habit>
}