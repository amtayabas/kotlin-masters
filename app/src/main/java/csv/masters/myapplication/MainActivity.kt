package csv.masters.myapplication

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.local.OrderManager
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.ActivityMainBinding
import csv.masters.myapplication.presentation.basket.adapter.BasketItemAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var enabled = false
    private var dataStoreManager: DataStoreManager? = null
    private var orderManager: OrderManager? = null
    private var orderItemAdapter: BasketItemAdapter? = null
    private var orders: ArrayList<Product> = arrayListOf()

    private var isOrderOnTheWay: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreManager(this)
        orderManager = OrderManager(dataStoreManager!!)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        title = getString(R.string.my_order)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupView()
        setupRecyclerView()
        setupBasket()

        MainScope().launch {
            delay(5000)
            setupProgressBarView()
        }
    }

    private fun setupView() {

        with(binding) {
            tvOrderTitle.text = getString(R.string.order_prepared)
            tvOrderETA.text = getString(R.string.order_eta)
            tvDriverName.text = getString(R.string.rider_name)
            tvRating.text = getString(R.string.rating)
            tvVehicleType.text = getString(R.string.vehicle_type)
            tvOrderSummary.text = getString(R.string.order_summary)

            btnCancelOrder.text = getString(R.string.cancel_order)

            btnCancelOrder.isEnabled = enabled
            btnCancelOrder.background = if (enabled) ContextCompat.getDrawable(
                applicationContext,
                R.drawable.round_button)
            else ContextCompat.getDrawable(
                applicationContext,
                R.drawable.round_button_disabled)
        }
    }

    private fun setupRecyclerView() {
        orderItemAdapter = BasketItemAdapter()
        binding.orderItemsRecyclerView.apply {
            adapter = orderItemAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupBasket() {
        lifecycleScope.launch {
            orders = orderManager!!.Operations().getUpcomingOrders()

            if (orders.isNotEmpty()) {
                binding.tvOrderSummary.visibility = View.VISIBLE
                binding.orderItemsRecyclerView.visibility = View.VISIBLE
            } else {
                binding.tvOrderSummary.visibility = View.GONE
                binding.orderItemsRecyclerView.visibility = View.GONE
            }

            orderItemAdapter?.differ?.submitList(orders)
            orderItemAdapter?.setIsOrderOnTheWay(true)

        }
    }

    private fun setupProgressBarView() {
        MainScope().launch {
            isOrderOnTheWay = true

            binding.loadingIcon.visibility = View.VISIBLE
            binding.loadingView.visibility = View.VISIBLE
            Glide.with(this@MainActivity)
                .load(R.drawable.loading_potato)
                .into(binding.loadingIcon)

            delay(3000)
            setUpOrderIsOnTheWayView()
        }
    }

    private fun setUpOrderIsOnTheWayView() {
        MainScope().launch {
            binding.loadingIcon.visibility = View.GONE
            binding.loadingView.visibility = View.GONE
            if (isOrderOnTheWay) {
                with(binding) {
                    tvOrderTitle.text = getString(R.string.order_on_the_way)
                    Glide.with(this@MainActivity)
                        .load(R.drawable.img_map)
                        .into(imgBanner)
                }
            }
            isOrderOnTheWay = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
            orderManager!!.Operations().emptyUpcomingOrders()
        }
        onBackPressed()
        return true
    }
}