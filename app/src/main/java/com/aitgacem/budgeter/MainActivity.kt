package com.aitgacem.budgeter

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_activity_host) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val fullScreenDest = setOf(
            R.id.details_dest,
            R.id.formFill_dest,
            R.id.settings_dest,
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in fullScreenDest) {
                bottomBar.visibility = GONE
            } else {
                bottomBar.visibility = VISIBLE
            }
        }
        bottomBar.setupWithNavController(navController)

    }


}



