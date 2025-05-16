package com.example.cleanaf.di

import android.content.Context
import androidx.room.Room
import com.example.cleanaf.data.AppDatabase
import com.example.cleanaf.data.TaskDao
import com.example.cleanaf.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "task-db").build()
    }


    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideTaskRepository(dao: TaskDao): TaskRepository = TaskRepository(dao)
}
