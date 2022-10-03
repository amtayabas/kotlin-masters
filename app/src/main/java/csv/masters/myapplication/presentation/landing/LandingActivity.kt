package csv.masters.myapplication.presentation.landing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.remote.api.ProductsApi
import csv.masters.myapplication.data.remote.api.RetrofitClient
import csv.masters.myapplication.data.repository.ProductsRepositoryImpl
import csv.masters.myapplication.databinding.ActivityLandingBinding
import csv.masters.myapplication.presentation.home.HomeActivity
import csv.masters.myapplication.presentation.home.HomeFragment
import csv.masters.myapplication.presentation.login.LoginActivity
import csv.masters.myapplication.presentation.signup.SignUpOptionActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    private var dataStoreManager: DataStoreManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        dataStoreManager = DataStoreManager(applicationContext)
        lifecycleScope.launchWhenCreated {
            binding.progressBar.visibility = View.VISIBLE
            fetchProducts()
        }

    }

    private fun setupView() {
        with(binding) {
            btnLogin.setOnClickListener{
                startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
            }
            btnSignup.setOnClickListener {
                startActivity(Intent(this@LandingActivity, SignUpOptionActivity::class.java))
            }
        }
    }

    private suspend fun fetchProducts() {
        val api = ProductsRepositoryImpl(RetrofitClient.getInstance().create(ProductsApi::class.java))
        try {
            val response = api.getProducts()
            if (response.isSuccessful) {
                val coffeeItems = response.body()!!
                dataStoreManager!!.putObject(Constants.Basket.MENU, coffeeItems, ArrayList::class.java)
                binding.progressBar.visibility = View.GONE

                if (dataStoreManager!!.getBoolean(Constants.User.SIGNED_IN) == true) {
                    startActivity(Intent(this@LandingActivity, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
            } else {
                Log.e(LOG_TAG, "Error encountered while fetching products: ${response.message()}")
                binding.progressBar.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error: ${e.localizedMessage}")
        }
    }
    
    companion object {
        private val LOG_TAG = LandingActivity::class.java.simpleName
    }
}