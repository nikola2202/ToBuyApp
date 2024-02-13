package com.example.tobuy.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.launch

class ToBuyViewModel:ViewModel() {

    private lateinit var repository: ToBuyRepository

    val itemEntitiesLiveData = MutableLiveData<List<ItemEntity>>()
    val itemWithCategoryEntitiesLiveData = MutableLiveData<List<ItemWithCategoryEntity>>()
    val categoryEntitiesLiveData = MutableLiveData<List<CategoryEntity>>()

    val transactionCompleteLiveData = MutableLiveData<Event<Boolean>>()

    private val _categoriesViewStateLiveData = MutableLiveData<CategoriesViewState>()
    val categoriesViewStateLiveData: LiveData<CategoriesViewState>
        get() = _categoriesViewStateLiveData

    fun init(appDatabase: AppDatabase) {
        repository = ToBuyRepository(appDatabase)

        //Initialize Flow connectivity to the DB
        viewModelScope.launch {
            repository.getAllItems().collect { items ->
                itemEntitiesLiveData.postValue(items)
            }
        }
        viewModelScope.launch {
            repository.getAllItemWithCategoryEntities().collect { items ->
                itemWithCategoryEntitiesLiveData.postValue(items)
            }
        }
        viewModelScope.launch {
            repository.getAllCategories().collect {categories->
                categoryEntitiesLiveData.postValue(categories)
            }
        }
    }

    fun onCategorySelected(categoryId: String,showLoading: Boolean = false) {
        if (showLoading) {
            val loadingViewState = CategoriesViewState(isLoading = true)
            _categoriesViewStateLiveData.value = loadingViewState
        }

        val categories = categoryEntitiesLiveData.value?: return
        val viewStateItemList = ArrayList<CategoriesViewState.Item>()

        // Default category (unselecting a category)
        viewStateItemList.add(CategoriesViewState.Item(
            categoryEntity = CategoryEntity.getDefaultCategory(),
            isSelected = categoryId == CategoryEntity.DEFAULT_CATEGORY_ID
        ))

        categories.forEach {
            viewStateItemList.add(CategoriesViewState.Item(
                categoryEntity = it,
                isSelected = it.id == categoryId
            ))
        }

        val viewState = CategoriesViewState(itemList = viewStateItemList)
        _categoriesViewStateLiveData.postValue(viewState)

    }

    data class CategoriesViewState(
        val isLoading: Boolean = false,
        val itemList: List<Item> = emptyList()
        ) {
        data class Item(
            val categoryEntity: CategoryEntity = CategoryEntity(),
            val isSelected: Boolean = false
        )

        fun getSelectedCategoryId(): String {
            return itemList.find { it.isSelected }?.categoryEntity?.id?: CategoryEntity.DEFAULT_CATEGORY_ID
        }

    }

    // region itemEntity
    fun insertItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.insertItem(itemEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }

    fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(itemEntity)
        }
    }

    fun updateItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.updateItem(itemEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }
    // endregion itemEntity

    //region CategoryEntity
    fun insertCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.insertCategory(categoryEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }
    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(categoryEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }
    //endregion CategoryEntity

}