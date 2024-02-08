package com.example.tobuy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.BaseFragment
import com.example.tobuy.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileEpoxyController = ProfileEpoxyController(
        onCategoryEmptyStateClicked = ::onCategoryEmptyStateClicked
    )
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

    private fun onCategoryEmptyStateClicked() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}