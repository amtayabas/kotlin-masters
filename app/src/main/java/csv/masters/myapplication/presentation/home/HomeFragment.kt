package csv.masters.myapplication.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import csv.masters.myapplication.data.local.BasketManager
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.remote.api.ProductsApi
import csv.masters.myapplication.data.remote.api.RetrofitClient
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponseItem
import csv.masters.myapplication.data.repository.ProductsRepositoryImpl
import csv.masters.myapplication.databinding.FragmentHomeBinding
import csv.masters.myapplication.presentation.home.adapter.CoffeeGroupAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coffeeItems: ArrayList<CoffeeResponseItem>

    private var dataStoreManager: DataStoreManager? = null
    private var basketManager: BasketManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.recyclerViewName.layoutManager = LinearLayoutManager(context)

        dataStoreManager = DataStoreManager(requireContext())
        basketManager = BasketManager(dataStoreManager!!)

        fetchProducts()

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
    }

    private fun fetchProducts() {
        val api = ProductsRepositoryImpl(RetrofitClient.getInstance().create(ProductsApi::class.java))
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            try {
                val response = api.getProducts()
                if (response.isSuccessful) {
                    coffeeItems = response.body()!!
                    setupAdapterList()
                } else {
                    Log.e(LOG_TAG, "Error encountered while fetching products: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error: ${e.localizedMessage}")
            }
        }
    }

    private fun setupAdapterList() {
        val adapter = CoffeeGroupAdapter()
        binding.recyclerViewName.adapter = adapter

        adapter.setOnItemClickListener {
            Log.d(LOG_TAG, "Selected Product: $it")
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it))
        }

        adapter.setOnAddToCartClickListener {
            Log.d(LOG_TAG, "Add to basket: ${it.name}")
            viewLifecycleOwner.lifecycleScope.launch {
                basketManager!!.Operations().addToBasket(it)
            }
            Toast.makeText(requireContext(), "${it.name} added to basket", Toast.LENGTH_SHORT).show()
        }

        adapter.differ.submitList(coffeeItems)
    }

    companion object {
        private val LOG_TAG = HomeFragment::class.java.simpleName
    }
}