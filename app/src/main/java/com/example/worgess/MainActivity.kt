package com.example.worgess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.worgess.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarID)

        //code for app configuration
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragContainerForNavigationHostId)
                    as NavHostFragment

        val navController = navHostFragment.navController

        val appBarConfigurationBuilder = AppBarConfiguration.Builder(navController.graph)
        appBarConfigurationBuilder.setOpenableLayout(binding.drawerLayoutId)

        val appBarConfiguration = appBarConfigurationBuilder.build()
        binding.toolbarID.setupWithNavController(navController, appBarConfiguration)

        binding.navigationViewId.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.help, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.fragContainerForNavigationHostId)

        return item.onNavDestinationSelected(navController) ||
                super.onOptionsItemSelected(item)
    }
}