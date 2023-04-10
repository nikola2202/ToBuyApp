package com.example.tobuy

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tobuy.dao.ItemEntityDao
import com.example.tobuy.model.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 1
)
abstract class AppDatabase:RoomDatabase() {
    abstract fun itemEntityDao(): ItemEntityDao
}