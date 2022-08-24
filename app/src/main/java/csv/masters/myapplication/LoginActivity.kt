package csv.masters.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import csv.masters.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnFb.text = "Coonect with Facebook"
            btnGoogle.text = "Connect with Google"
            btnApple.text = "Connect with Apple"
            btnNumber.text = "Connect with Number"

            btnFb.setOnClickListener {
                gotoNextPage()
            }

            btnGoogle.setOnClickListener {
                gotoNextPage()
            }

            btnApple.setOnClickListener {
                gotoNextPage()
            }

            btnNumber.setOnClickListener {
                gotoNextPage()
            }
        }
    }

    private fun gotoNextPage() {
       // val intent = Intent(this@LoginActivity::class.java)
        startActivity(intent)
    }
}


