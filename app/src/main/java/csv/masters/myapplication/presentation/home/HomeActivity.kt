package csv.masters.myapplication.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivityHomeBinding
import csv.masters.myapplication.presentation.landing.LandingActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView() {
        setSupportActionBar(binding.toolbar)
        val drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        val navController = findNavController(R.id.navFragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.basketFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { item ->
            when(item.title) {
                getString(R.string.sign_out) -> {
                    openLandingPage()
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
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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