package com.example.tobuy.ui.profile

import com.example.tobuy.database.entity.CategoryEntity

interface ProfileInterface {

    fun onCategoryEmptyStateClicked()
    fun onDeleteCategory(categoryEntity: CategoryEntity)
    fun onCategorySelected(categoryEntity: CategoryEntity)
    fun onPrioritySelected(priorityName: String)

}