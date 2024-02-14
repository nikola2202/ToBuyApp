package com.example.tobuy.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.BaseFragment
import com.example.tobuy.R
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment(), ProfileInterface {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileEpoxyController = ProfileEpoxyController(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.epoxyRecyclerView.setController(profileEpoxyController)

        sharedViewModel.categoryEntitiesLiveData.observe(viewLifecycleOwner) {categoryEntityList->
            profileEpoxyController.categories = categoryEntityList
        }
    }

    override fun onDeleteCategory(categoryEntity: CategoryEntity) {
        sharedViewModel.deleteCategory(categoryEntity)
    }

    override fun onCategorySelected(categoryEntity: CategoryEntity) {
        Log.i("ProfileFragment", categoryEntity.toString())
    }

    override fun onPrioritySelected(priorityName: String) {
        navigateViaNavGraph(ProfileFragmentDirections
            .actionProfileFragmentToAddCategoryEntityFragment(priorityName)
        )
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(requireView())
    }

    override fun onCategoryEmptyStateClicked() {
        navigateViaNavGraph(R.id.action_profileFragment_to_addCategoryEntityFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}