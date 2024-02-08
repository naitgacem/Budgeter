package com.aitgacem.budgeter

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aitgacem.budgeter.data.TransactionDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

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



