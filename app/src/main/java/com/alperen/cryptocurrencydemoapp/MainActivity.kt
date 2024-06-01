package com.alperen.cryptocurrencydemoapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.alperen.cryptocurrencydemoapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        handleBottomBarVisibility()
        with(binding) {
            fabTrade.setOnClickListener { navigateToChartFragment() }
        }
    }

    private fun navigateToChartFragment() {
        navController.navigate(R.id.action_tradeFragment_to_chartFragment)
    }

    private fun handleBottomBarVisibility() {
        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, bundle: Bundle? ->
            if (destination.id == R.id.chartFragment) {
                binding.bottomBar.visibility = View.GONE
                binding.fabTrade.visibility = View.GONE
            } else {
                binding.bottomBar.visibility = View.VISIBLE
                binding.fabTrade.visibility = View.VISIBLE
            }
        }
    }
}