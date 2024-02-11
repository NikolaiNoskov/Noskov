package com.example.topmovie.utils

import android.content.Context
import android.content.res.Configuration
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.topmovie.R
import com.example.topmovie.controllers.fragments.FavouriteFragments
import com.example.topmovie.controllers.fragments.InfoFragment


// side:
//     true -> left
//     false -> right

object NavigationManager {
    fun navigateTo(context: Context, manager : FragmentManager, toFragment: Fragment, isLeft : Boolean) {
        manager.beginTransaction()
            .replace(getContainer(context, isLeft), toFragment,"OPERATION" )
            .addToBackStack("TRANSACTION")
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out,
            )
            .commit()
    }

    fun navigateWithExtrasTo(context: Context, manager : FragmentManager,  extra : Int, isLeft : Boolean) {
        manager.beginTransaction()
            .replace(getContainer(context, isLeft), InfoFragment.newInstance(extra),"OPERATION" )
            .addToBackStack("TRANSACTION")
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out,
            )
            .commit()
    }

    private fun getContainer(context: Context, isLeft: Boolean): Int {
        val result : Int
        val orientation : Boolean = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        result = if(orientation) {
            R.id.cont_main
        } else if(isLeft){
            R.id.cont_left
        } else{
            R.id.cont_right
        }
        return result
    }
}

