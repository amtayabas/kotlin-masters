package csv.masters.myapplication.presentation.productdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import csv.masters.myapplication.R
import csv.masters.myapplication.data.local.BasketManager
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.FragmentProductDetailBinding
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding

    private var dataStoreManager: DataStoreManager? = null
    private var basketManager: BasketManager? = null

    private var selectedProduct: Product? = null
    private var isUpdatingBasket: Boolean = false
    private var quantityCounter: Int = 1
    private var totalPrice: Float = 0.0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        val args = arguments?.let {
            ProductDetailFragmentArgs.fromBundle(it)
        }

        if (args != null) {
            isUpdatingBasket = args.editProduct
            selectedProduct = args.selectedProduct

            if (isUpdatingBasket) {
                setupUpdateView()
            } else {
                setupView()
            }

            dataStoreManager = DataStoreManager(requireContext())
            basketManager = BasketManager(dataStoreManager!!)
            Log.d(">>args", args.toString())
        } else {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (dataStoreManager != null) {
            dataStoreManager = null
        }

        if (basketManager != null) {
            basketManager = null
        }

        if (selectedProduct != null) {
            selectedProduct = null
        }
    }

    private fun setupView() {
        with(binding) {
            selectedProduct?.let { prod ->
                tvProduct.text = prod.name
                tvPrice.text = String.format("Php %.2f", prod.price)
                tvDescription.text = prod.description

                Glide.with(requireContext())
                    .load(prod.image)
                    .into(ivProduct)

                totalPrice = computeTotalPrice(quantityCounter, prod.price)

                val buttonText = getString(R.string.add_to_basket, String.format("%.2f", totalPrice))
                buttonAdd.text = buttonText

                setupSizeOptions(prod, false)
                setupAddOnOptions(prod, false)

                buttonAdd.setOnClickListener {
                    selectedProduct!!.quantity = quantityCounter
                    selectedProduct!!.totalProductPrice = totalPrice
                    Log.d(LOG_TAG, "Add to basket: ${selectedProduct!!.name}")
                    viewLifecycleOwner.lifecycleScope.launch {
                        basketManager!!.Operations().addToBasket(selectedProduct!!)
                        Toast.makeText(
                            requireContext(),
                            "${selectedProduct!!.name} added to basket",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun setupUpdateView() {
        with(binding) {
            selectedProduct?.let { prod ->

                val productPrice = prod.price
                quantityCounter = prod.quantity

                tvProduct.text = prod.name
                tvPrice.text = String.format("Php %.2f", prod.price)
                tvDescription.text = prod.description

                Glide.with(requireContext())
                    .load(prod.image)
                    .into(ivProduct)

                textViewQuantity.text = getString(R.string.quantity, prod.quantity.toString())

                val buttonText = getString(R.string.update_basket, String.format("%.2f", prod.totalProductPrice))
                buttonAdd.text = buttonText

                when (prod.size) {
                    getString(R.string.size_tall) -> rbTall.isChecked = true
                    getString(R.string.size_large) -> rbLarge.isChecked = true
                    else -> rbRegular.isChecked = true
                }

                prod.addOn?.let {
                    for(item in it) {
                        when(item) {
                            getString(R.string.caramel_drizzle) -> binding.cbCaramel.isChecked = true
                            getString(R.string.hazelnut_syrup) -> binding.cbHazelnut.isChecked = true
                            getString(R.string.toffee_nut_syrup) -> binding.cbToffee.isChecked = true
                        }
                    }
                }

                setupSizeOptions(prod, true)
                setupAddOnOptions(prod, true)

                quantityLayout.visibility = View.VISIBLE
                btnPlus.setOnClickListener {
                    quantityCounter++
                    textViewQuantity.text = getString(R.string.quantity, quantityCounter.toString())
                    totalPrice = computeTotalPrice(quantityCounter, productPrice, prod.size, prod.addOn)
                    buttonAdd.text = getString(R.string.update_basket, String.format("%.2f", totalPrice))
                }

                btnMinus.setOnClickListener {
                    if (quantityCounter > 1) {
                        quantityCounter--
                        textViewQuantity.text = getString(R.string.quantity, quantityCounter.toString())
                        totalPrice = computeTotalPrice(quantityCounter, productPrice, prod.size, prod.addOn)
                        buttonAdd.text = getString(R.string.update_basket, String.format("%.2f", totalPrice))
                    }
                }

                buttonAdd.setOnClickListener {
                    selectedProduct!!.quantity = quantityCounter
                    selectedProduct!!.totalProductPrice = totalPrice
                    Log.d(LOG_TAG, "Update basket: ${selectedProduct!!.name}")
                    viewLifecycleOwner.lifecycleScope.launch {
                        basketManager!!.Operations().updateFromBasket(selectedProduct!!)
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun setupSizeOptions(prod: Product, isUpdate: Boolean? = false) {
        binding.rgSize.setOnCheckedChangeListener { _, index ->
            when (index) {
                R.id.rbRegular -> {
                    prod.size = getString(R.string.size_regular)
                }
                R.id.rbTall -> {
                    prod.size = getString(R.string.size_tall)
                }
                R.id.rbLarge -> {
                    prod.size = getString(R.string.size_large)
                }
            }
            updateTotalPrice(prod, isUpdate)
        }
    }

    private fun setupAddOnOptions(prod: Product, isUpdate: Boolean? = false) {
        binding.cbCaramel.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                prod.addOn = prod.addOn ?: arrayListOf()
                prod.addOn?.add(getString(R.string.caramel_drizzle))
            } else {
                if (prod.addOn?.contains(getString(R.string.caramel_drizzle)) == true) {
                    prod.addOn?.remove(getString(R.string.caramel_drizzle))
                }
            }
            updateTotalPrice(prod, isUpdate)
        }

        binding.cbHazelnut.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                prod.addOn = prod.addOn ?: arrayListOf()
                prod.addOn?.add(getString(R.string.hazelnut_syrup))
            } else {
                if (prod.addOn?.contains(getString(R.string.hazelnut_syrup)) == true) {
                    prod.addOn?.remove(getString(R.string.hazelnut_syrup))
                }
            }
            updateTotalPrice(prod, isUpdate)
        }

        binding.cbToffee.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                prod.addOn = prod.addOn ?: arrayListOf()
                prod.addOn?.add(getString(R.string.toffee_nut_syrup))
            } else {
                if (prod.addOn?.contains(getString(R.string.toffee_nut_syrup)) == true) {
                    prod.addOn?.remove(getString(R.string.toffee_nut_syrup))
                }
            }
            updateTotalPrice(prod, isUpdate)
        }
    }

    private fun updateTotalPrice(prod: Product, isUpdate: Boolean? = false) {
        totalPrice = computeTotalPrice(quantityCounter, prod.price, size = prod.size, addOn = prod.addOn ?: arrayListOf())
        if (isUpdate == true) {
            binding.buttonAdd.text = getString(R.string.update_basket, String.format("%.2f", totalPrice))
        } else {
            binding.buttonAdd.text = getString(R.string.add_to_basket, String.format("%.2f", totalPrice))
        }
    }

    private fun computeTotalPrice(counter: Int, price: Float, size: String? = "", addOn: ArrayList<String>? = arrayListOf()): Float {
        var subTotal = price

        subTotal +=
            when (size) {
                getString(R.string.size_tall) -> 10.0f
                getString(R.string.size_large) -> 20.0f
                else -> 0.0f
            }

        if (addOn?.isNotEmpty() == true) {
            subTotal += 20.0f * addOn.size
        }

        return subTotal * counter
    }

    companion object {
        private val LOG_TAG = ProductDetailFragment::class.java.simpleName
    }
}