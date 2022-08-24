package csv.masters.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import csv.masters.myapplication.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnLogin.setOnClickListener{
                val intent = Intent(this@LandingActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            btnSignup.setOnClickListener {
                val intent = Intent(this@LandingActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}