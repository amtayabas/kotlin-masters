package csv.masters.myapplication.presentation.basket

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import csv.masters.myapplication.R
import csv.masters.myapplication.databinding.ActivityBasketBinding

class BasketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {

            tvMyBasket.text = "MY BASKET"
            tvDeliverTo.text = "Deliver to"
            tvOrderSummary.text = "Order Summary"
            tvPaymentDetails.text = "Payment Details"
            rbCash.text = "Cash"
            rbCreditCard.text = "Credit Card"
            tvTotal.text = "Total"
            buttonPlaceOrder.text = "PlaceOrder"


            fun onRadioButtonClicked(view: View) {
                if (view is RadioButton) {
                    val checked = view.isChecked
                    when (view.getId()) {
                        R.id.rbCash ->
                            if (checked) {
                                rbCash.isChecked = true
                            }
                        R.id.rbCreditCard ->
                            if (checked) {
                                rbCreditCard.isChecked = false

                            }
                    }
                }
            }

            buttonPlaceOrder.setOnClickListener() {
                Toast.makeText(this@BasketActivity, "Order Successful", Toast.LENGTH_LONG).show()
            }
        }


//        initRecyclerView()
//        calculateTotalamount()
//    }
//
//    private fun initRecyclerView() {
//        TODO("Not yet implemented")
//    }
//
//    private fun calculateTotalamount() {
//        TODO("Not yet implemented")
//    }

    }
}

