package csv.masters.myapplication.presentation.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import csv.masters.myapplication.R
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponse
import csv.masters.myapplication.databinding.FragmentBasketBinding
import csv.masters.myapplication.presentation.home.HomeFragment


class BasketFragment : Fragment() {


    private lateinit var binding: FragmentBasketBinding
    private lateinit var basketItems: CoffeeResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)

   setupView()

        return binding.root
    }

    private fun setupView() {
            with(binding) {

                tvMyBasket.text = "MY BASKET"
                tvDeliverTo.text = "Deliver to"
                tvOrderSummary.text = "Order Summary"
                tvPaymentDetails.text = "Payment Details"
                rbCash.text = "Cash"
                rbCreditCard.text = "Credit Card (VISA9524)"
                tvTotal.text = "Total"
                buttonPlaceOrder.text = "Place Order"


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
                    Toast.makeText(requireActivity(), "Order Successful", Toast.LENGTH_LONG).show()
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

    companion object {
        val LOG_TAG = HomeFragment::class.java.simpleName
    }
}


