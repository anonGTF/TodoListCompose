package com.galih.noteappcompose.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galih.noteappcompose.data.model.TodoModel

@Database(
    entities = [TodoModel::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao
}