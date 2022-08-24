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

        setupView()
    }

    private fun setupView() {
        with(binding) {
            btnLogin.setOnClickListener{
                startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
            }
            btnSignup.setOnClickListener {
                Toast.makeText( this@LandingActivity,"Signup Button Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}