package com.example.tobuy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.tobuy.BaseFragment
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentHomeBinding
import com.example.tobuy.ui.home.bottomsheet.SortOrderBottomSheetDialogFragment

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

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_home_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.menuItemSort) {
                    SortOrderBottomSheetDialogFragment().show(childFragmentManager,null)
                    true
                }else{
                    onMenuItemSelected(menuItem)
                }
            }
        },viewLifecycleOwner)

        binding.fab.setOnClickListener {
            navigateViaNavGraph(R.id.action_homeFragment_to_addItemEntityFragment)
        }

        val controller = HomeEpoxyController(this)
        binding.epoxyRecyclerView.setController(controller)

        sharedViewModel.homeViewStateLiveData.observe(viewLifecycleOwner) { viewState->
            controller.viewState = viewState
        }

        //Swipe-to-delete
        EpoxyTouchHelper.initSwiping(binding.epoxyRecyclerView)
            .right()
            .withTarget(HomeEpoxyController.ItemEntityEpoxyModel::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<HomeEpoxyController.ItemEntityEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: HomeEpoxyController.ItemEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val removedItem = model?.itemEntity?: return
                    sharedViewModel.deleteItem(removedItem.itemEntity)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(requireView())
    }

    override fun onBumpPriority(itemEntity: ItemEntity) {
        val currentPriority = itemEntity.priority
        var newPriority = currentPriority + 1
        if (newPriority > 3) {
            newPriority = 1
        }
        val updatedItemEntity = itemEntity.copy(priority = newPriority)
        sharedViewModel.updateItem(updatedItemEntity)
    }

    override fun onItemSelected(itemEntity: ItemEntity) {
        val navDirections = HomeFragmentDirections.actionHomeFragmentToAddItemEntityFragment(itemEntity.id)
        navigateViaNavGraph(navDirections)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}