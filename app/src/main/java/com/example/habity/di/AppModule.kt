package com.example.habity.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.habity.feature_habit.data.data_source.local.HabitDatabase
import com.example.habity.feature_habit.data.data_source.remote.ApiService
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.data.network.WebSocketClient
import com.example.habity.feature_habit.data.repository.LocalRepositoryImpl
import com.example.habity.feature_habit.data.repository.RemoteRepositoryImpl
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository
import com.example.habity.feature_habit.domain.use_case.AddEditUseCase
import com.example.habity.feature_habit.domain.use_case.DeleteHabitUseCase
import com.example.habity.feature_habit.domain.use_case.GetHabitUseCase
import com.example.habity.feature_habit.domain.use_case.GetHabitsUseCase
import com.example.habity.feature_habit.domain.use_case.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:2419")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(okHttpClient: OkHttpClient): WebSocketClient {
        return WebSocketClient(okHttpClient)
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(apiService: ApiService): RemoteRepository {
        return RemoteRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideMealDatabase(app: Application): HabitDatabase {
        return Room.databaseBuilder(
            app,
            HabitDatabase::class.java,
            HabitDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(db: HabitDatabase): LocalRepository {
        return LocalRepositoryImpl(db.habitDao)
    }

    @Singleton
    @Provides
    fun provideNetworkStatusChecker(@ApplicationContext context: Context): NetworkStatusChecker {
        return NetworkStatusChecker(context)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        localRepository: LocalRepository,
        remoteRepository: RemoteRepository,
        networkStatusChecker: NetworkStatusChecker
    ): UseCases {
        return UseCases(
            getHabitsUseCase = GetHabitsUseCase(
                localRepository,
                remoteRepository,
                networkStatusChecker
            ),
            getHabitUseCase = GetHabitUseCase(
                localRepository,
                remoteRepository,
                networkStatusChecker
            ),
            addEditUseCase = AddEditUseCase(
                localRepository,
                remoteRepository,
                networkStatusChecker
            ),
            deleteHabitUseCase = DeleteHabitUseCase(
                localRepository,
                remoteRepository,
                networkStatusChecker
            )
        )
    }
}