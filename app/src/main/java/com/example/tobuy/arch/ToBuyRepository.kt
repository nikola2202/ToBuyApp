package com.example.tobuy.arch

import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.ItemEntity

class ToBuyRepository(
    private val appDatabase: AppDatabase
) {

    fun insertItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().insert(itemEntity)
    }

    fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().delete(itemEntity)
    }

    suspend fun getAllItems(): List<ItemEntity> {
        return appDatabase.itemEntityDao().getAllItemEntities()
    }
}