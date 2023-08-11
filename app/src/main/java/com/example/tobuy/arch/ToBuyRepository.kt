package com.example.tobuy.arch

import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

class ToBuyRepository(
    private val appDatabase: AppDatabase
) {

    suspend fun insertItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().insert(itemEntity)
    }

    suspend fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().delete(itemEntity)
    }

    suspend fun updateItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().update(itemEntity)
    }

    fun getAllItems(): Flow<List<ItemEntity>> {
        return appDatabase.itemEntityDao().getAllItemEntities()
    }
}