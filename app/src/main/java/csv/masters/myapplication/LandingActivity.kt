package csv.masters.myapplication

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
                Toast.makeText(this@LandingActivity, "Login Button Clicked", Toast.LENGTH_SHORT).show()
            }
            btnSignup.setOnClickListener {
                Toast.makeText( this@LandingActivity,"Signup Button Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}