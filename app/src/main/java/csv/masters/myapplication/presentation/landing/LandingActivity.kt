package csv.masters.myapplication.presentation.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.databinding.ActivityLandingBinding
import csv.masters.myapplication.presentation.home.HomeActivity
import csv.masters.myapplication.presentation.login.LoginActivity
import csv.masters.myapplication.presentation.signup.SignUpOptionActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    private var dataStoreManager: DataStoreManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreManager(applicationContext)

        lifecycleScope.launchWhenCreated {
            if (dataStoreManager!!.getBoolean(Constants.User.SIGNED_IN) == true) {
                startActivity(Intent(this@LandingActivity, HomeActivity::class.java))
            }
        }

        setupView()
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
}