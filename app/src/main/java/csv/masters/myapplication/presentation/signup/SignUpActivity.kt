package csv.masters.myapplication.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivitySignUpBinding
import csv.masters.myapplication.databinding.LayoutSignupHeaderBinding
import csv.masters.myapplication.presentation.signup.ConfirmCodeActivity.Companion.PHONE_NUMBER

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var headerBinding: LayoutSignupHeaderBinding
    private lateinit var mobileNoError: TextView
    private lateinit var emailAddError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        headerBinding = binding.header
        headerBinding.tvTitle.text = getString(R.string.signUp)
        headerBinding.ivBack.setOnClickListener {
            onBackPressed()
        }

        with(binding) {
            tvPhoneNumber.text = getString(R.string.phone_number)
            tvEmail.text = getString(R.string.email)
            tvName.text = getString(R.string.name)
            btnNext.text = getString(R.string.next_button)
            tvFooterText.text = getString(R.string.footer_text)
            tvMobileNoError.text = getString(R.string.invalid_mobile_number)
            this@SignUpActivity.mobileNoError = tvMobileNoError
            tvMobileNoError.visibility = View.GONE
            tvEmailAddError.text = getString(R.string.email_error)
            this@SignUpActivity.emailAddError = tvEmailAddError
            tvEmailAddError.visibility = View.GONE
        }

        binding.btnNext.setOnClickListener {
            if (isTextFieldNotEmpty()) {
                if (isPhoneNumberValid()) {
                    if (isEmailValid()) {
                        val intent = Intent(this, ConfirmCodeActivity::class.java).apply {
                            putExtra(
                                PHONE_NUMBER,
                                binding.ccp.selectedCountryCode + binding.phoneNumber.text
                            )
                        }
                        startActivity(intent)
                    } else {
                        emailAddError.visibility = View.VISIBLE
                    }
                } else {
                    mobileNoError.visibility = View.VISIBLE
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

    private fun isPhoneNumberValid(): Boolean {
        return Patterns.PHONE.matcher(binding.phoneNumber.text.toString()).matches() &&
                binding.phoneNumber.text?.length!! >= 10
    }
}