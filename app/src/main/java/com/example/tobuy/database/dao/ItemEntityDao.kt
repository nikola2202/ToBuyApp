package com.example.tobuy.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tobuy.database.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemEntityDao {

    @Query("SELECT * FROM item_entity ")
    fun getAllItemEntities(): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itemEntity: ItemEntity)

    @Delete
    suspend fun delete(itemEntity: ItemEntity)

    @Update
    suspend fun update(itemEntity: ItemEntity)

}