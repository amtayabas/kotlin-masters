package csv.masters.myapplication.presentation.basket

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivityBasketBinding

class BasketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBasketBinding
    lateinit var scrollView: ScrollView
    lateinit var rbCash: RadioButton
    lateinit var rbCreditCard: RadioButton
    lateinit var SelectLocation: EditText
    lateinit var cartItemsRecyclerView: RecyclerView
    lateinit var tvTotalAmount:TextView
    lateinit var buttonPlaceYourOrder:Button
    lateinit var placeYourOrderAdapter: BasketActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
    }

    SelectLocation = findViewById(R.id.etSelectLocation)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceOrder)
        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView)


       // buttonPlaceYourOrder.setOnClickListener {
       //     startActivity(Intent(this@BasketActivity, ::class.java))
        }

    }
