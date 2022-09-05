package csv.masters.myapplication.presentation.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import csv.masters.myapplication.presentation.signup.ConfirmCodeActivity.Companion.PHONE_NUMBER
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivitySignUpBinding
import csv.masters.myapplication.databinding.LayoutSignupHeaderBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var headerBinding: LayoutSignupHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        headerBinding = binding.header
        headerBinding.tvTitle.text = getString(R.string.signUp)

        with(binding) {
            tvPhoneNumber.text = getString(R.string.phone_number)
            tvEmail.text = getString(R.string.email)
            tvName.text = getString(R.string.name)
            btnNext.text = getString(R.string.next_button)
            tvFooterText.text = getString(R.string.footer_text)
        }

        binding.btnNext.setOnClickListener {
            if (isTextFieldNotEmpty()) {
                if (isEmailValid()) {
                    val intent = Intent(this, ConfirmCodeActivity::class.java).apply {
                        putExtra(PHONE_NUMBER, binding.ccp.selectedCountryCode + binding.phoneNumber.text)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.empty_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isTextFieldNotEmpty(): Boolean {
        with(binding) {
            return email.text.toString().isNotEmpty() && phoneNumber.text.toString().isNotEmpty() && name.text.toString().isNotEmpty()
        }
    }

    private fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()
    }
}