package csv.masters.myapplication.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import csv.masters.myapplication.R
import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.databinding.ActivityHomeBinding
import csv.masters.myapplication.presentation.landing.LandingActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private var dataStoreManager: DataStoreManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        dataStoreManager = DataStoreManager(applicationContext)
    }

    private fun setUpView() {
        setSupportActionBar(binding.toolbar)
        val drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        val navController = findNavController(R.id.navFragment)
        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, drawerLayout)

        navView.setNavigationItemSelectedListener { item ->
            when (item.title) {
                getString(R.string.sign_out) -> {
                    lifecycleScope.launch {
                        dataStoreManager!!.putBoolean(Constants.User.SIGNED_IN, false)
                        dataStoreManager!!.deleteData(Constants.Basket.BASKET_OBJECT)
                        openLandingPage()
                    }
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
            }
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

    }

    // NOTE: Can be used for settings (Kebab Menu)
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_drawer, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navFragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun openLandingPage() {
        startActivity(
            Intent(
                this@HomeActivity,
                LandingActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
    }

}
