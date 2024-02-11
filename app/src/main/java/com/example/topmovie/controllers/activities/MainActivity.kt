package com.example.topmovie.controllers.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.topmovie.R
import com.example.topmovie.controllers.fragments.InfoFragment
import com.example.topmovie.controllers.fragments.MainFragment
import com.example.topmovie.utils.NavigationManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(getContainerByOrientation(), MainFragment(),"OPERATION")
            .commit()
    }


    private fun getContainerByOrientation(): Int {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> R.id.cont_main
            Configuration.ORIENTATION_LANDSCAPE -> R.id.cont_left
            else -> 0
        }
    }

}
