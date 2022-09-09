package csv.masters.myapplication.presentation.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivityLoginBinding
import csv.masters.myapplication.presentation.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
            btnFb.text = getString(R.string.continue_with_facebook)
            btnGoogle.text = getString(R.string.continue_with_google)
            btnApple.text = getString(R.string.continue_with_apple)
            btnNumber.text = getString(R.string.continue_with_number)

            btnFb.setOnClickListener {
                gotoNextPage("Facebook")
            }

            btnGoogle.setOnClickListener {
                gotoNextPage("Google")
            }

            btnApple.setOnClickListener {
                gotoNextPage("Apple")
            }

            btnNumber.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
        }
    }

    private fun gotoNextPage(nextPage: String) {
       Toast.makeText(applicationContext, "Continue with $nextPage...", Toast.LENGTH_SHORT).show()
    }
}


