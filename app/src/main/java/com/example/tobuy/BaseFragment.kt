package com.example.tobuy

import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.database.AppDatabase
import com.google.android.material.color.MaterialColors

abstract class BaseFragment:Fragment() {

    protected val mainActivity: MainActivity
        get() = activity as MainActivity

    protected val appDatabase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

    protected val sharedViewModel: ToBuyViewModel by activityViewModels()
    protected fun navigateUp() {
        mainActivity.navController.navigateUp()
    }
    protected fun navigateViaNavGraph(actionId:Int) {
        mainActivity.navController.navigate(actionId)
    }
    protected fun navigateViaNavGraph(navDirections:NavDirections) {
        mainActivity.navController.navigate(navDirections)
    }

}