package com.aitgacem.budgeter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        showPrivacyDialog()
        setupFirebase()

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


    private fun showPrivacyDialog() {
        val sharedPref = getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()

        if (!sharedPref.getBoolean("first_start", true)) {
            return
        }

        editor.putBoolean("first_start", false)
        editor.apply()
        val header = LayoutInflater.from(this).inflate(R.layout.dialog_privacy_title, null, false)
        MaterialAlertDialogBuilder(this)
            .setCustomTitle(header)
            .setNeutralButton(resources.getString(R.string.learn_more)) { dialog, which ->

            }
            .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, which ->
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                editor.apply()
            }
            .setMultiChoiceItems(
                R.array.privacy_dialog_choices,
                BooleanArray(2) { false }) { _, which, isChecked ->
                when (which) {
                    0 -> editor.putBoolean(
                        "firebase_crashlytics", isChecked
                    )

                    1 -> editor.putBoolean(
                        "google_analytics", isChecked
                    )

                    else -> {

                    }
                }

            }
            .show()

    }

    private fun setupFirebase() {
        firebaseAnalytics = Firebase.analytics

        val crashReportEnabled =
            getDefaultSharedPreferences(this).getBoolean("firebase_crashlytics", false)
        val googleAnalyticsEnabled =
            getDefaultSharedPreferences(this).getBoolean("google_analytics", false)

        Firebase.crashlytics.setCrashlyticsCollectionEnabled(crashReportEnabled)
        Firebase.analytics.setAnalyticsCollectionEnabled(googleAnalyticsEnabled)
    }
}



