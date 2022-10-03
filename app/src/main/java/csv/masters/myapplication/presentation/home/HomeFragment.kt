package csv.masters.myapplication.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import csv.masters.myapplication.R
import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.local.BasketManager
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponseItem
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.FragmentHomeBinding
import csv.masters.myapplication.databinding.LayoutHomeWithBasketBinding
import csv.masters.myapplication.presentation.home.adapter.CoffeeGroupAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var basketBinding: LayoutHomeWithBasketBinding

    private var coffeeItems: ArrayList<CoffeeResponseItem> = arrayListOf()

    private var dataStoreManager: DataStoreManager? = null
    private var basketManager: BasketManager? = null
    private var progressBar: ProgressBar? = null
    private var quantityCounter: Int = 1
    private var basketSize: Int = 0
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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            getBasket()
            fetchProducts()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private suspend fun fetchProducts() {
        coffeeItems = dataStoreManager!!.getObjectList(Constants.Basket.MENU, CoffeeResponseItem::class.java)?: arrayListOf()
        progressBar!!.visibility = View.GONE
        setupAdapterList(coffeeItems)
    }

    private suspend fun getBasket() {
        basketSize = 0
        subtotal = 0.0f
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

    private fun setupAdapterList(valueList: ArrayList<CoffeeResponseItem>) { //added parameter valueList [Sham]
        val adapter = CoffeeGroupAdapter()
        binding.recyclerViewName.adapter = adapter

        adapter.setOnItemClickListener {
            Log.d(LOG_TAG, "Selected Product: $it")
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it))
        }

        adapter.setOnAddToCartClickListener {
            val totalProductPrice = it.price * quantityCounter
            it.quantity = quantityCounter
            it.totalProductPrice = totalProductPrice
            if (it.size.isNullOrEmpty()) it.size = "Regular (12oz)"
            Log.d(LOG_TAG, "Add to basket: ${it.name}")
            viewLifecycleOwner.lifecycleScope.launch {
                basketManager!!.Operations().addToBasket(it)
            }

            basketSize += quantityCounter
            subtotal += totalProductPrice
            setUpBasketView(basketSize, basketSize, subtotal)
        }

        adapter.differ.submitList(valueList) //changed from coffeeList to the function's parameter which is the valueList [Sham]
    }

    private fun setUpBasketView(quantity: Int, itemInBasket: Int, total: Float) {
        val itemQuantity = requireContext().resources.getQuantityString(R.plurals.items_plural, quantity, itemInBasket)
        basketBinding.layout.visibility = View.VISIBLE
        basketBinding.buttonBasket.text =
            String.format(getString(R.string.button_basket), itemQuantity, String.format("Php %.2f", total))

        binding.layoutBasket.buttonBasket.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBasketFragment())
        }
    }

    private fun setUpSearchView() { //newly added function for searchView [Sham]
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchKey: String?): Boolean {

                var isProductExisting = false
                var searchResult: ArrayList<Product> = arrayListOf()

                //looping through coffee items to check if searchKey is existing
                for (item in coffeeItems) {
                    for (product in item.products) {
                        if (product.name.contains(searchKey.toString(), ignoreCase = true)) {
                            isProductExisting = true
                            searchResult.add(product)
                        }
                    }
                }

                if (isProductExisting) {
                    var coffeeItemsSearched: ArrayList<CoffeeResponseItem> = arrayListOf()
                    coffeeItemsSearched.add(CoffeeResponseItem("Search Result", searchResult))
                    setupAdapterList(coffeeItemsSearched)
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