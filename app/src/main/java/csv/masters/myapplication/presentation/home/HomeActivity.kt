package csv.masters.myapplication.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivityHomeBinding
import csv.masters.myapplication.presentation.basket.BasketFragment
import csv.masters.myapplication.presentation.productdetail.ProductDetailFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment
        setupActionBarWithNavController(navHostFragment.navController)

        binding.apply {
            toggle = ActionBarDrawerToggle(this@HomeActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Coffee Master"

            navigationView.setNavigationItemSelectedListener {
                it.isChecked = true

                when(it.itemId) {
                    R.id.homeFragment -> {
                        replaceFragment(HomeFragment(), "Home")
                    }
                    R.id.basketFragment -> {
                        replaceFragment(BasketFragment(), "Basket")
                    }
                    R.id.productDetailFragment -> {
                        replaceFragment(ProductDetailFragment(), "Product Detail")
                    }
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navFragment, fragment)
        fragmentTransaction.commit()
        binding.drawerLayout.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}