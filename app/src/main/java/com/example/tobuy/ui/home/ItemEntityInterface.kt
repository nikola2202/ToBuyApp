package com.example.tobuy.ui.home

import com.example.tobuy.database.entity.ItemEntity

interface ItemEntityInterface {
    fun onBumpPriority(itemEntity: ItemEntity)
    fun onItemSelected(itemEntity: ItemEntity)
}
