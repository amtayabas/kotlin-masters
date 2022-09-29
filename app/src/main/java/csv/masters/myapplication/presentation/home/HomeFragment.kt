package csv.masters.myapplication.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import csv.masters.myapplication.R
import csv.masters.myapplication.data.local.BasketManager
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.remote.api.ProductsApi
import csv.masters.myapplication.data.remote.api.RetrofitClient
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponseItem
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.data.repository.ProductsRepositoryImpl
import csv.masters.myapplication.databinding.FragmentHomeBinding
import csv.masters.myapplication.databinding.LayoutHomeWithBasketBinding
import csv.masters.myapplication.presentation.home.adapter.CoffeeGroupAdapter
import csv.masters.myapplication.presentation.home.adapter.CoffeeItemAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var basketBinding: LayoutHomeWithBasketBinding
    private lateinit var coffeeItems: ArrayList<CoffeeResponseItem>

    private var dataStoreManager: DataStoreManager? = null
    private var basketManager: BasketManager? = null
    private var progressBar: ProgressBar? = null
    private var quantityCounter: Int = 1
    private var basketSize : Int = 0
    private var basket: ArrayList<Product> = arrayListOf()
    private var subtotal: Float = 0.0f
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.recyclerViewName.layoutManager = LinearLayoutManager(context)

        basketBinding = binding.layoutBasket

        dataStoreManager = DataStoreManager(requireContext())
        basketManager = BasketManager(dataStoreManager!!)

        progressBar = binding.progressBar
        progressBar!!.visibility = View.VISIBLE

        searchView = binding.searchView

        fetchProducts()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBasket()
        setUpSearchView()
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
                    progressBar!!.visibility = View.GONE
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

    private fun getBasket(){
        basketSize = 0
        subtotal = 0.0f
        viewLifecycleOwner.lifecycleScope.launch {
            basket = basketManager!!.Operations().getBasket()

            basketSize = basket.size

            for (item in basket) {
                subtotal += item.totalProductPrice
            }

            if (basketSize > 0) {
                binding.layoutBasket.layout.visibility = View.VISIBLE
                setUpBasketView(basketSize, basketSize, subtotal)
            } else {
                binding.layoutBasket.layout.visibility = View.GONE
            }

            Log.d(LOG_TAG, "Basket Size$basketSize")
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
            val totalProductPrice = it.price*quantityCounter
            it.quantity = quantityCounter
            it.totalProductPrice = totalProductPrice
            Log.d(LOG_TAG, "Add to basket: ${it.name}")
            viewLifecycleOwner.lifecycleScope.launch {
                basketManager!!.Operations().addToBasket(it)
            }

            basketSize += quantityCounter
            subtotal += totalProductPrice
            setUpBasketView(basketSize, basketSize, subtotal)
        }

        adapter.differ.submitList(coffeeItems)
    }

    private fun setUpBasketView(quantity: Int, itemInBasket: Int, total: Float) {
        val itemQuantity = requireContext().resources.getQuantityString(R.plurals.items_plural, quantity, itemInBasket)
        basketBinding.layout.visibility = View.VISIBLE
        basketBinding.buttonBasket.text = String.format(getString(R.string.button_basket), itemQuantity, String.format("Php %.2f", total))

        binding.layoutBasket.buttonBasket.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBasketFragment())
        }
    }

    private fun setUpSearchView() {

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var isProductExisting = false
                for (item in coffeeItems) {
                    var coffeeProducts = item.products
                    for (cProducts in coffeeProducts) {
                        if (cProducts.name == query) {
                            isProductExisting = true
                        }
                    }
                }
                if (isProductExisting) {
                    Toast.makeText(requireContext(), "Product found", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Product NOT found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
    }

    companion object {
        private val LOG_TAG = HomeFragment::class.java.simpleName
    }
}