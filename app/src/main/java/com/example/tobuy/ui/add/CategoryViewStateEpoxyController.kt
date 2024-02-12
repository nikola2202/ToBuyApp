package com.example.tobuy.ui.add

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.R
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.databinding.ModelCategoryBinding
import com.example.tobuy.databinding.ModelCategoryItemSelectionBinding
import com.example.tobuy.getAttrColor
import com.example.tobuy.ui.epoxy.LoadingEpoxyModel
import com.example.tobuy.ui.epoxy.ViewBindingKotlinModel

class CategoryViewStateEpoxyController: EpoxyController() {
    var viewState = ToBuyViewModel.CategoriesViewState()
        set(value) {
            field = value
            requestModelBuild()
        }
    override fun buildModels() {

        if (viewState.isLoading) {
            LoadingEpoxyModel().id("Loading").addTo(this)
            return
        }

        viewState.itemList.forEach { item ->
            CategoryViewStateItem(item).id(item.categoryEntity.id).addTo(this)
        }
    }
    data class CategoryViewStateItem(
        val item: ToBuyViewModel.CategoriesViewState.Item
    ): ViewBindingKotlinModel<ModelCategoryItemSelectionBinding>(R.layout.model_category_item_selection) {
        override fun ModelCategoryItemSelectionBinding.bind() {
            textView.text = item.categoryEntity.name

            val colorRes = if (item.isSelected) com.airbnb.viewmodeladapter.R.attr.colorSecondary
            else androidx.appcompat.R.attr.colorPrimary
            textView.setTextColor(root.getAttrColor(colorRes))
        }

    }

}