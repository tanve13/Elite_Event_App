package com.tanveer.eventplannerproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

import com.tanveer.eventplannerproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    private var navController: NavController? = null
    var appBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        if (sharedPref.getBoolean("dark", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.bottomNav?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navController?.navigate(R.id.mainFragment)
                R.id.history -> navController?.navigate(R.id.historyFragment)
                R.id.menu -> navController?.navigate(R.id.menuFragment)
            }
            return@setOnItemSelectedListener true
        }
        navController = findNavController(R.id.host)
        appBarConfiguration = navController?.graph?.let {
            AppBarConfiguration(it)
        }
        setupActionBarWithNavController(navController!!, appBarConfiguration!!)
        navController?.addOnDestinationChangedListener { navController, destination, arguments ->
            when (destination.id) {
                R.id.mainFragment -> {
                    supportActionBar?.title =
                        resources.getString(R.string.event)
                    binding?.bottomNav?.menu?.findItem(R.id.home)?.isChecked = true
                }

                R.id.historyFragment -> {
                    supportActionBar?.title =
                        resources.getString(R.string.history)
                    binding?.bottomNav?.menu?.findItem(R.id.history)?.isChecked = true
                }

                R.id.menuFragment -> {
                    supportActionBar?.title =
                        resources.getString(R.string.Menu)
                    binding?.bottomNav?.menu?.findItem(R.id.menu)?.isChecked = true
                }

                R.id.singleEventDetailFragment -> supportActionBar?.title =
                    resources.getString(R.string.event_details)

                R.id.guestFragment -> supportActionBar?.title = resources.getString(R.string.Guest)
                R.id.vendorFragment -> supportActionBar?.title =
                    resources.getString(R.string.Vendor)

                R.id.budgetFragment -> supportActionBar?.title =
                    resources.getString(R.string.Budget)

            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController!!.popBackStack()
    }
}

