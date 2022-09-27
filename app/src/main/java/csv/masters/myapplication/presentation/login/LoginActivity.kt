package csv.masters.myapplication.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivityLoginBinding
import csv.masters.myapplication.databinding.LayoutSignupHeaderBinding

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
    }
}