package csv.masters.myapplication.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivityLoginBinding
import csv.masters.myapplication.databinding.LayoutSignupHeaderBinding
import csv.masters.myapplication.presentation.home.HomeActivity
import csv.masters.myapplication.presentation.signup.ConfirmCodeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var headerBinding: LayoutSignupHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
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
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}