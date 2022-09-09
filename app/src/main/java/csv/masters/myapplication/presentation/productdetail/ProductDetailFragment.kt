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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        val args = arguments?.let {
            ProductDetailFragmentArgs.fromBundle(
                it
            )
        }

        if (args != null) {
            selectedProduct = args.selectedProduct
            dataStoreManager = DataStoreManager(requireContext())
            basketManager = BasketManager(dataStoreManager!!)
            setupView()
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
            selectedProduct?.let {
                tvProductName.text = it.name
                tvPrice.text = String.format("Php %.2f", it.price)
                tvDescription.text = it.description

                Glide.with(requireContext())
                    .load(it.image)
                    .into(ivProduct)

                val buttonText = "ADD TO BASKET - ${String.format("%.2f", it.price)}"

                btAddToBasket.text = buttonText
                btAddToBasket.setOnClickListener {
                    Log.d(LOG_TAG, "Add to basket: ${selectedProduct!!.name}")
                    viewLifecycleOwner.lifecycleScope.launch {
                        basketManager!!.Operations().addToBasket(selectedProduct!!)
                    }
                    Toast.makeText(requireContext(), "${selectedProduct!!.name} added to basket", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    companion object {
        private val LOG_TAG = ProductDetailFragment::class.java.simpleName
    }
}