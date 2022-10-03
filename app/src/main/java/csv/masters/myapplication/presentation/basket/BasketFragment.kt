package csv.masters.myapplication.presentation.basket

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding

    private var dataStoreManager: DataStoreManager? = null
    private var basketManager: BasketManager? = null
    private var basketItemAdapter: BasketItemAdapter? = null

    private var basket: ArrayList<Product> = arrayListOf()

    private var isButtonEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)

        dataStoreManager = DataStoreManager(requireContext())
        basketManager = BasketManager(dataStoreManager!!)

        setupView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBasket()
    }

    override fun onResume() {
        super.onResume()
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

            buttonPlaceOrder.setOnClickListener {
                //
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

            with(binding) {
                if (basket.isNotEmpty()) {
                    tvOrderSummary.visibility = View.VISIBLE
                    cartItemsRecyclerView.visibility = View.VISIBLE
                    ivDelete.visibility = View.VISIBLE
                    isButtonEnabled = true
                } else {
                    tvOrderSummary.visibility = View.GONE
                    cartItemsRecyclerView.visibility = View.GONE
                    ivDelete.visibility = View.GONE
                    isButtonEnabled = false
                }

                buttonPlaceOrder.isEnabled = isButtonEnabled
                buttonPlaceOrder.background = if (isButtonEnabled) ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_button)
                else ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_button_disabled)

                basketItemAdapter?.differ?.submitList(basket)

                if (basket.isNotEmpty()) {
                    var subtotal = 0.0
                    for (prod in basket) {
                        subtotal += prod.totalProductPrice
                    }
                    tvTotalAmount.text = String.format("Php %.2f", subtotal)
                } else {
                    tvTotalAmount.text = "Php 0.00"
                }
            }
        }
    }

    private fun clearBasket() {
        basketItemAdapter!!.differ.submitList(arrayListOf())

        with(binding) {
            tvOrderSummary.visibility = View.GONE
            cartItemsRecyclerView.visibility = View.GONE
            ivDelete.visibility = View.GONE
            tvTotalAmount.text = "Php 0.00"
            buttonPlaceOrder.isEnabled = false
            buttonPlaceOrder.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_button_disabled)
        }
    }

    companion object {
        private val LOG_TAG = BasketFragment::class.java.simpleName
    }
}