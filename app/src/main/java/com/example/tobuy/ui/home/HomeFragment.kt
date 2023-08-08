package com.example.tobuy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.BaseFragment
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentHomeBinding

class HomeFragment:BaseFragment(),ItemEntityInterface {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            navigateViaNavGraph(R.id.action_homeFragment_to_addItemEntityFragment)
        }

        val controller = HomeEpoxyController(this)
        binding.epoxyRecyclerView.setController(controller)

        sharedViewModel.itemEntitiesLiveData.observe(viewLifecycleOwner) {itemEntityList->
            controller.itemEntityList = itemEntityList as ArrayList<ItemEntity>
        }
    }
    override fun onDeleteItemEntity(itemEntity: ItemEntity) {
        //todo
    }

    override fun onBumpPriority(itemEntity: ItemEntity) {
        //todo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}