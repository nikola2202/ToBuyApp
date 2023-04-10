package com.example.tobuy.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tobuy.model.ItemEntity

@Dao
interface ItemEntityDao {

    @Query("SELECT * FROM item_entity ")
    fun getAll(): List<ItemEntity>

    @Insert
    fun insert(itemEntity: ItemEntity)

    @Delete
    fun delete(itemEntity:ItemEntity)

}