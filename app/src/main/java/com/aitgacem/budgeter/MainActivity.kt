package com.aitgacem.budgeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.aitgacem.budgeter.ui.fragments.OverviewFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

object CurrentActivityHolder {
    var currentActivity: WeakReference<ComponentActivity>? = null
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            val fragment = OverviewFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_activity_host, fragment)
                .commit()
        }
    }


}



