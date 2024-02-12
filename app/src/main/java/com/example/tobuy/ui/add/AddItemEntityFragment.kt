package com.example.tobuy.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.tobuy.BaseFragment
import com.example.tobuy.R
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentAddItemEntityBinding
import java.util.UUID

class AddItemEntityFragment:BaseFragment() {

    private var _binding : FragmentAddItemEntityBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: AddItemEntityFragmentArgs by navArgs()
    private val selectedItemEntity: ItemEntity? by lazy {
        sharedViewModel.itemWithCategoryEntitiesLiveData.value?.find {
            it.itemEntity.id == safeArgs.selectedItemEntityId
        }?.itemEntity
    }

    private var isInEditMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemEntityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            saveItemEntityToDatabase()
        }

        binding.quantitySeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val currentText = binding.titleEditText.text.toString().trim()
                if (currentText.isEmpty()) {
                    return
                }
                val startIndex = currentText.indexOf("[") - 1
                val newText = if (startIndex > 0 ) {
                    "${currentText.substring(0,startIndex)} [$p1]"
                } else {
                    "$currentText [$p1]"
                }

                val sanitizedText = newText.replace(" [1]","")

                binding.titleEditText.setText(sanitizedText)
                binding.titleEditText.setSelection(sanitizedText.length)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //Nothing to do
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //Nothing to do
            }

        })

        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                if (isInEditMode) {
                    navigateUp()
                    return@observe
                }
                Toast.makeText(requireActivity(),"Item saved",Toast.LENGTH_SHORT).show()

                binding.titleEditText.text = null
                binding.titleEditText.requestFocus()
                mainActivity.showKeyboard()

                binding.descriptionEditText.text = null
                binding.radioGroup.check(R.id.radioButtonLow)
            }
        }
        //Show keyboard and default select Title EditText
        binding.titleEditText.requestFocus()
        mainActivity.showKeyboard()

        //Setup screen if we are in edit mode
        selectedItemEntity?.let { itemEntity ->
            isInEditMode = true
            binding.titleEditText.setText(itemEntity.title)
            binding.titleEditText.setSelection(itemEntity.title.length)
            binding.descriptionEditText.setText(itemEntity.description)
            when(itemEntity.priority) {
                1 -> binding.radioGroup.check(R.id.radioButtonLow)
                2 -> binding.radioGroup.check(R.id.radioButtonMedium)
                else -> binding.radioGroup.check(R.id.radioButtonHigh)
            }
            binding.saveButton.text = "Update"
            mainActivity.supportActionBar?.title = "Update item"

            if (itemEntity.title.contains("[")) {
                val startIndex = itemEntity.title.indexOf("[") + 1
                val endIndex = itemEntity.title.indexOf("]")

                try {
                    val progress = itemEntity.title.substring(startIndex,endIndex).toInt()
                    binding.quantitySeekBar.progress = progress
                }catch (e: Exception) {
                    //Whoops
                }
            }
        }

        val categoryViewStateEpoxyController = CategoryViewStateEpoxyController()
        binding.categoryEpoxyController.setController(categoryViewStateEpoxyController)
        sharedViewModel.onCategorySelected(selectedItemEntity?.categoryId ?: CategoryEntity.DEFAULT_CATEGORY_ID)
        sharedViewModel.categoriesViewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            categoryViewStateEpoxyController.viewState = viewState
        }

    }
    private fun saveItemEntityToDatabase() {
        val itemTitle = binding.titleEditText.text.toString().trim()
        if (itemTitle.isEmpty()) {
            binding.titleTextField.error = "* Required field"
            return
        }
        binding.titleTextField.error = null

        var itemDescription: String? = binding.descriptionEditText.text.toString().trim()
        if (itemDescription?.isEmpty() == true) {
            itemDescription = null
        }
        val itemPriority = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioButtonLow -> 1
            R.id.radioButtonMedium -> 2
            R.id.radioButtonHigh -> 3
            else -> 0
        }

        if (isInEditMode) {
            val itemEntity = selectedItemEntity!!.copy(
                title = itemTitle,
                description = itemDescription,
                priority = itemPriority
            )

            sharedViewModel.updateItem(itemEntity)
            return
        }

        val itemEntity = ItemEntity(
            id = UUID.randomUUID().toString(),
            title = itemTitle,
            description = itemDescription,
            priority = itemPriority,
            createdAt = System.currentTimeMillis(),
            categoryId = "" // todo
        )

        sharedViewModel.insertItem(itemEntity)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}