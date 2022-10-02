package csv.masters.myapplication.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import csv.masters.myapplication.R
import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.databinding.ActivityLoginBinding
import csv.masters.myapplication.databinding.LayoutSignupHeaderBinding
import csv.masters.myapplication.presentation.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var headerBinding: LayoutSignupHeaderBinding

    private var dataStoreManager: DataStoreManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        dataStoreManager = DataStoreManager(applicationContext)
    }

    private fun setupView() {
        headerBinding = binding.header
        headerBinding.tvTitle.text = getString(R.string.login)
        headerBinding.ivBack.setOnClickListener {
            onBackPressed()
        }

        with(binding) {
            tvPhoneNumber.text = "Phone Number"
            tvPassword.text = "Password"
            btnNext.text = "Next"
        }

        binding.btnNext.setOnClickListener {
            if (isPhoneNumberValid()) {
                if (isPasswordCorrect()) {
                    lifecycleScope.launch {
                        dataStoreManager!!.putBoolean(Constants.User.SIGNED_IN, true)
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                } else {

                }
            } else {

            }
        }
    }

    private fun isPasswordCorrect(): Boolean {
        return binding.password.text.toString() == TEMP_PASSWORD
    }

    private fun isPhoneNumberValid(): Boolean {
        return Patterns.PHONE.matcher(binding.phoneNumber.text.toString()).matches() &&
                binding.phoneNumber.text?.length!! >= 10
    }

    companion object {
        private const val TEMP_PASSWORD = "Welcome@1"
    }
}