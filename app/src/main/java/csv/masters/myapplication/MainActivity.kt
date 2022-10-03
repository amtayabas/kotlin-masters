package csv.masters.myapplication

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import csv.masters.myapplication.data.local.BasketManager
import csv.masters.myapplication.data.local.DataStoreManager
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.ActivityMainBinding
import csv.masters.myapplication.presentation.basket.adapter.BasketItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var enabled = false
    private var dataStoreManager: DataStoreManager? = null
    private var basketManager: BasketManager? = null
    private var basketItemAdapter: BasketItemAdapter? = null
    private var basket: ArrayList<Product> = arrayListOf()

    private var loadingProgressBar: ProgressBar? = null

    private var isOrderOnTheWay: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreManager(this)
        basketManager = BasketManager(dataStoreManager!!)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        title = getString(R.string.my_order)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadingProgressBar = binding.progressBar

        setupView()
        setupRecyclerView()
        setupBasket()

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                startTimer()
            }
        }, 5000)
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
        basketItemAdapter = BasketItemAdapter()
        binding.orderItemsRecyclerView.apply {
            adapter = basketItemAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupBasket() {
        lifecycleScope.launch {
            basket = basketManager!!.Operations().getBasket()

            if (basket.isNotEmpty()) {
                binding.tvOrderSummary.visibility = View.VISIBLE
                binding.orderItemsRecyclerView.visibility = View.VISIBLE
            } else {
                binding.tvOrderSummary.visibility = View.GONE
                binding.orderItemsRecyclerView.visibility = View.GONE
            }

            basketItemAdapter?.differ?.submitList(basket)

        }
    }

    private fun startTimer() {
        MainScope().launch {
            withContext(Dispatchers.Main) {

            }
            isOrderOnTheWay = true

            loadingProgressBar!!.visibility = View.VISIBLE
            binding.loadingView.visibility = View.VISIBLE

            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    setUpOrderIsOnTheWayView()
                }
            }, 3000)
        }
    }

    private fun setUpOrderIsOnTheWayView() {
        MainScope().launch {
            withContext(Dispatchers.Main) {

            }
            loadingProgressBar!!.visibility = View.GONE
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
            basketManager!!.Operations().emptyBasket()
        }
        onBackPressed()
        return true
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.homeFragment, HomeFragment())
//            commit()
//        }
//    }
}