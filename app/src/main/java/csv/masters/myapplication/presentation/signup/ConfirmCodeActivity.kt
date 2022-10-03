package csv.masters.myapplication.presentation.signup

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import csv.masters.myapplication.R
import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.databinding.ActivityConfirmCodeBinding
import csv.masters.myapplication.databinding.LayoutSignupHeaderBinding
import csv.masters.myapplication.presentation.home.HomeActivity
import kotlinx.coroutines.launch

class ConfirmCodeActivity : AppCompatActivity() {

    private lateinit var headerBinding: LayoutSignupHeaderBinding
    private lateinit var binding: ActivityConfirmCodeBinding

    private var dataStoreManager: DataStoreManager? = null

    private var phoneNumber: String? = null

    private var remainingRetry = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra(PHONE_NUMBER)

        setupView()
        dataStoreManager = DataStoreManager(applicationContext)
    }

    private fun setupView() {
        headerBinding = binding.header
        headerBinding.tvTitle.text = getString(R.string.sign_up)

        with(binding) {
            tvConfirmCodeTitle.text = getString(R.string.confirm_code_title)

            phoneNumber?.let {
                val phoneNumber = "+$it"
                tvPhoneNumber.text = phoneNumber
            }

            tvCodeTitle.text = getString(R.string.code)
            tvResendTitle.text = getString(R.string.request_code_title)
            tvRequestNewCode.text = getString(R.string.request_new_code)
            tvRequestNewCode.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            etConfirmationCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // NOTE: No implementation
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (start + count == CODE_LENGTH) {
                        if (s.toString() != CODE) {
                            val errorMessage: String
                            remainingRetry -= 1
                            if (remainingRetry > 0) {
                                errorMessage = String.format(getString(R.string.error_confirm_code), remainingRetry)
                            } else {
                                errorMessage = getString(R.string.error_confirm_failed)
                                etConfirmationCode.isEnabled = false
                            }

                            etConfirmationCode.text?.clear()
                            tvErrorMessage.text = errorMessage
                            tvErrorMessage.visibility = View.VISIBLE
                        } else {
                            tvErrorMessage.visibility = View.GONE
                            lifecycleScope.launch {
                                remainingRetry = MAX_RETRY
                                dataStoreManager!!.putBoolean(Constants.User.SIGNED_IN, true)
                                startActivity(Intent(this@ConfirmCodeActivity, HomeActivity::class.java))
                            }
                        }
                    } else {
                        tvErrorMessage.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // NOTE: No implementation
                }
            })

            tvRequestNewCode.setOnClickListener {
                etConfirmationCode.isEnabled = true
                etConfirmationCode.text?.clear()
                tvErrorMessage.visibility = View.GONE
                remainingRetry = MAX_RETRY
            }
        }
    }

    companion object {
        const val PHONE_NUMBER = "phone_number"

        private const val CODE = "123456"
        private const val CODE_LENGTH = 6
        private const val MAX_RETRY = 3
    }
}