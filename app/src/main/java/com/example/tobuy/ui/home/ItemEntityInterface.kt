package com.example.tobuy.ui.home

import com.example.tobuy.database.entity.ItemEntity

interface ItemEntityInterface {
    fun onDeleteItemEntity (itemEntity: ItemEntity)
    fun onBumpPriority(itemEntity: ItemEntity)
}
