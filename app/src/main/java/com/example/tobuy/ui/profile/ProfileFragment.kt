package com.example.tobuy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.BaseFragment
import com.example.tobuy.R
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

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(requireView())
    }

    private fun onCategoryEmptyStateClicked() {
        navigateViaNavGraph(R.id.action_profileFragment_to_addCategoryEntityFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}