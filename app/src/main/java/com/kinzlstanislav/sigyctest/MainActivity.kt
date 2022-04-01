package com.kinzlstanislav.sigyctest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigationRouter
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val nestedNavigationRouter by inject<NestedNavigationRouter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_App)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        nestedNavigationRouter.attachNavigationController(navController)
    }
}