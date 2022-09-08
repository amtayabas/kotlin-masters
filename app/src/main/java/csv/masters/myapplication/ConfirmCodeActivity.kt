package csv.masters.myapplication

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import csv.masters.myapplication.databinding.ActivityConfirmCodeBinding
import csv.masters.myapplication.databinding.LayoutSignupHeaderBinding

class ConfirmCodeActivity : AppCompatActivity() {

    private lateinit var headerBinding: LayoutSignupHeaderBinding
    private lateinit var binding: ActivityConfirmCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        headerBinding = binding.header

        setupView()

    }

    private fun setupView() {
        headerBinding.tvTitle.text = getString(R.string.sign_up)

        with(binding) {
            tvConfirmCodeTitle.text = getString(R.string.confirm_code_title)

            // TODO: Get from user input
            tvPhoneNumber.text = "+63 910 123 4567"

            tvCodeTitle.text = getString(R.string.code)
            tvErrorMessage.text = getString(R.string.error_confirm_code)
            tvResendTitle.text = getString(R.string.request_code_title)
            tvRequestNewCode.text = getString(R.string.request_new_code)
            tvRequestNewCode.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            etConfirmationCode.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // TODO: ("Not yet implemented")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (start + count == CODE_LENGTH) {
                        if (s != CODE) {
                            tvErrorMessage.visibility = View.VISIBLE
                        } else {
                            tvErrorMessage.visibility = View.GONE
                        }
                    } else {
                        tvErrorMessage.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // TODO: ("Not yet implemented")
                }
            })

            tvRequestNewCode.setOnClickListener {
                etConfirmationCode.text?.clear()
                tvErrorMessage.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val CODE = "123456"
        private const val CODE_LENGTH = 6
    }
}