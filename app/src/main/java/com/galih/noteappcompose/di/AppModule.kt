package com.galih.noteappcompose.di

import android.app.Application
import androidx.room.Room
import com.galih.noteappcompose.data.datasource.local.BaseDatabase
import com.galih.noteappcompose.data.datasource.local.TodoDao
import com.galih.noteappcompose.data.mapper.Mapper
import com.galih.noteappcompose.data.mapper.TodoMapper
import com.galih.noteappcompose.data.model.TodoModel
import com.galih.noteappcompose.data.repoimpl.TodoRepositoryImpl
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.repository.TodoRepository
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
    fun provideBaseDatabase(app: Application): BaseDatabase =
        Room
            .databaseBuilder(app, BaseDatabase::class.java, "todo_db")
            .build()

    @Provides
    @Singleton
    fun provideTodoDao(db: BaseDatabase): TodoDao = db.getTodoDao()

    @Provides
    @Singleton
    fun provideTodoMapper(): Mapper<TodoModel, Todo> = TodoMapper()

    @Provides
    @Singleton
    fun provideTodoRepository(
        db: TodoDao,
        mapper: Mapper<TodoModel, Todo>
    ): TodoRepository = TodoRepositoryImpl(db, mapper)
}