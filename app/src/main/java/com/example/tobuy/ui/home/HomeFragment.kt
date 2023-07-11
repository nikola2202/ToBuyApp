package com.example.tobuy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.BaseFragment
import com.example.tobuy.databinding.FragmentHomeBinding

class HomeFragment:BaseFragment() {

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
        sharedViewModel.itemEntitiesLiveData.observe(viewLifecycleOwner) {itemEntityList->

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}