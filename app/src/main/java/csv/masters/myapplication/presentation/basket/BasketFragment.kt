package csv.masters.myapplication.presentation.basket

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import csv.masters.myapplication.R
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponse
import csv.masters.myapplication.databinding.FragmentBasketBinding
import csv.masters.myapplication.presentation.home.HomeFragment

=======
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import csv.masters.myapplication.R
import csv.masters.myapplication.data.local.BasketManager
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.FragmentBasketBinding
import csv.masters.myapplication.presentation.basket.adapter.BasketItemAdapter
import kotlinx.coroutines.launch
>>>>>>> 37f20f120c9677cfa4cecbbced0648e66aa44888

class BasketFragment : Fragment() {


    private lateinit var binding: FragmentBasketBinding
    private lateinit var basketItems: CoffeeResponse

    private var dataStoreManager: DataStoreManager? = null
    private var basketManager: BasketManager? = null
    private var basketItemAdapter: BasketItemAdapter? = null

    private var basket: ArrayList<Product> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)

<<<<<<< HEAD
   setupView()
=======
        dataStoreManager = DataStoreManager(requireContext())
        basketManager = BasketManager(dataStoreManager!!)

        setupView()
>>>>>>> 37f20f120c9677cfa4cecbbced0648e66aa44888

        return binding.root
    }

<<<<<<< HEAD
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
=======
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBasket()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (dataStoreManager != null) {
            dataStoreManager = null
        }

        if (basketManager != null) {
            basketManager = null
        }

        if (basketItemAdapter != null) {
            basketItemAdapter = null
        }
    }


    private fun setupView() {
        with(binding) {
            Glide.with(requireContext())
                .load(R.drawable.ic_delete)
                .into(ivDelete)

            ivDelete.setOnClickListener {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Oh No!")
                    .setMessage("Do you want to clear your basket?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            basketManager!!.Operations().emptyBasket()
                            clearBasket()
                        }
                    }
                    .setNegativeButton("No") { _, _ ->

                    }
                    .create().show()
            }
        }
    }

    private fun setupRecyclerView() {
        basketItemAdapter = BasketItemAdapter()
        binding.cartItemsRecyclerView.apply {
            adapter = basketItemAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupBasket() {
        viewLifecycleOwner.lifecycleScope.launch {
            basket = basketManager!!.Operations().getBasket()

            if (basket.isNotEmpty()) {
                binding.tvOrderSummary.visibility = View.VISIBLE
                binding.cartItemsRecyclerView.visibility = View.VISIBLE
            } else {
                binding.tvOrderSummary.visibility = View.GONE
                binding.cartItemsRecyclerView.visibility = View.GONE
            }

            basketItemAdapter?.differ?.submitList(basket)

            if (basket.isNotEmpty()) {
                var subtotal = 0.0
                for (prod in basket) {
                    subtotal += prod.totalProductPrice
                }
                binding.tvTotalAmount.text = String.format("Php %.2f", subtotal)
            } else {
                binding.tvTotalAmount.text = "Php 0.00"
            }
        }
    }

    private fun clearBasket() {
        basketItemAdapter!!.differ.submitList(arrayListOf())
        binding.tvOrderSummary.visibility = View.GONE
        binding.cartItemsRecyclerView.visibility = View.GONE
        binding.tvTotalAmount.text = "Php 0.00"
    }

    companion object {
        private val LOG_TAG = BasketFragment::class.java.simpleName
>>>>>>> 37f20f120c9677cfa4cecbbced0648e66aa44888
    }

    companion object {
        val LOG_TAG = HomeFragment::class.java.simpleName
    }
}


