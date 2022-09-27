package csv.masters.myapplication.presentation.productdetail

import android.content.Intent
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
<<<<<<< HEAD
import csv.masters.myapplication.presentation.basket.BasketFragment
import csv.masters.myapplication.presentation.signup.SignUpActivity

=======
import kotlinx.coroutines.launch
>>>>>>> 37f20f120c9677cfa4cecbbced0648e66aa44888

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


        setupView()
    }

<<<<<<< HEAD
    private fun setupView() {
        with(binding) {

            ButtonAdd.setOnClickListener {
                activity?.let {
                    val intent = Intent(it, BasketFragment::class.java)
                    it.startActivity(intent)
                }
            }
        }
=======
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
            selectedProduct?.let {
                tvProductName.text = it.name
                tvPrice.text = String.format("Php %.2f", it.price)
                tvDescription.text = it.description

                Glide.with(requireContext())
                    .load(it.image)
                    .into(ivProduct)

                totalPrice = computeTotalPrice(quantityCounter, it.price)

                val buttonText = getString(R.string.add_to_basket, String.format("%.2f", totalPrice))

                btAddToBasket.text = buttonText
                btAddToBasket.setOnClickListener {
                    selectedProduct!!.quantity = quantityCounter
                    selectedProduct!!.totalProductPrice = totalPrice
                    Log.d(LOG_TAG, "Add to basket: ${selectedProduct!!.name}")
                    viewLifecycleOwner.lifecycleScope.launch {
                        basketManager!!.Operations().addToBasket(selectedProduct!!)
                    }
                    Toast.makeText(requireContext(), "${selectedProduct!!.name} added to basket", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun setupUpdateView() {
        with(binding) {
            selectedProduct?.let {

                val productPrice = it.price
                quantityCounter = it.quantity

                tvProductName.text = it.name
                tvPrice.text = String.format("Php %.2f", it.price)
                tvDescription.text = it.description

                Glide.with(requireContext())
                    .load(it.image)
                    .into(ivProduct)

                textViewQuantity.text = getString(R.string.quantity, it.quantity.toString())

                val buttonText = getString(R.string.update_basket, String.format("%.2f", it.totalProductPrice))
                btAddToBasket.text = buttonText

                quantityLayout.visibility = View.VISIBLE
                btnPlus.setOnClickListener {
                    quantityCounter++
                    textViewQuantity.text = getString(R.string.quantity, quantityCounter.toString())
                    setUpdateButtonText(quantityCounter, productPrice)
                    totalPrice = computeTotalPrice(quantityCounter, productPrice)
                }

                btnMinus.setOnClickListener {
                    if (quantityCounter > 1) {
                        quantityCounter--
                        textViewQuantity.text = getString(R.string.quantity, quantityCounter.toString())
                        setUpdateButtonText(quantityCounter, productPrice)
                        totalPrice = computeTotalPrice(quantityCounter, productPrice)
                    }
                }

                btAddToBasket.setOnClickListener {
                        selectedProduct!!.quantity = quantityCounter
                        selectedProduct!!.totalProductPrice = totalPrice
                        Log.d(LOG_TAG, "Update basket: ${selectedProduct!!.name}")
                        viewLifecycleOwner.lifecycleScope.launch {
                            basketManager!!.Operations().updateFromBasket(selectedProduct!!)
                        }
                        Toast.makeText(requireContext(), "${selectedProduct!!.name} updated to basket", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpdateButtonText(counter: Int, price: Float) {
        with(binding) {
            val totalPrice = price*counter
            val buttonText = getString(R.string.update_basket, String.format("%.2f", totalPrice))
            btAddToBasket.text = buttonText
        }
    }

    private fun computeTotalPrice(counter: Int, price: Float): Float {
        return price*counter
    }

    companion object {
        private val LOG_TAG = ProductDetailFragment::class.java.simpleName
>>>>>>> 37f20f120c9677cfa4cecbbced0648e66aa44888
    }

}

