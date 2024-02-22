package com.example.habity.di

import android.app.Application
import androidx.room.Room
import com.example.habity.feature_habit.data.data_source.HabitDatabase
import com.example.habity.feature_habit.data.repository.LocalRepositoryImpl
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.use_case.AddEditUseCase
import com.example.habity.feature_habit.domain.use_case.DeleteHabitUseCase
import com.example.habity.feature_habit.domain.use_case.GetHabitUseCase
import com.example.habity.feature_habit.domain.use_case.GetHabitsUseCase
import com.example.habity.feature_habit.domain.use_case.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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

    @Provides
    @Singleton
    fun provideUseCases(localRepository: LocalRepository): UseCases {
        return UseCases(
            getHabitsUseCase = GetHabitsUseCase(localRepository),
            getHabitUseCase = GetHabitUseCase(localRepository),
            addEditUseCase = AddEditUseCase(localRepository),
            deleteHabitUseCase = DeleteHabitUseCase(localRepository)
        )
    }
}