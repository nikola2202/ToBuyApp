package com.example.tobuy.ui.profile

import android.app.AlertDialog
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.R
import com.example.tobuy.addHeaderModel
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.databinding.ModelCategoryBinding
import com.example.tobuy.databinding.ModelEmptyButtonBinding
import com.example.tobuy.ui.epoxy.ViewBindingKotlinModel

class ProfileEpoxyController(
    private val profileInterface: ProfileInterface
) : EpoxyController() {

    var categories: List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }
    override fun buildModels() {

        // Categories section
        addHeaderModel("Categories")

        categories.forEach {
            CategoryEpoxyModel(it, profileInterface).id(it.id).addTo(this)
        }

        EmptyButtonEpoxyModel("Add Category",profileInterface)
            .id("add_category")
            .addTo(this)
    }
    data class CategoryEpoxyModel(
        val categoryEntity: CategoryEntity,
        val profileInterface: ProfileInterface
    ): ViewBindingKotlinModel<ModelCategoryBinding>(R.layout.model_category) {
        override fun ModelCategoryBinding.bind() {
            textView.text = categoryEntity.name
            root.setOnClickListener {
                profileInterface.onCategorySelected(categoryEntity)
            }
            root.setOnLongClickListener {
                AlertDialog.Builder(it.context)
                    .setTitle("Delete ${categoryEntity.name}?")
                    .setPositiveButton("Yes") {_,_ ->
                        profileInterface.onDeleteCategory(categoryEntity)
                    }
                    .setNegativeButton("Cancel") { _,_ ->

                    }
                    .show()
                return@setOnLongClickListener true
            }
        }
    }

    data class EmptyButtonEpoxyModel(
        val buttonText: String,
        val profileInterface: ProfileInterface
    ): ViewBindingKotlinModel<ModelEmptyButtonBinding>(R.layout.model_empty_button) {
        override fun ModelEmptyButtonBinding.bind() {
            button.text = buttonText
            button.setOnClickListener{profileInterface.onCategoryEmptyStateClicked() }
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }

    }
}