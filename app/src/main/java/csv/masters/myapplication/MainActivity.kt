package csv.masters.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import csv.masters.myapplication.databinding.ActivityMainBinding
import csv.masters.myapplication.databinding.LayoutMainHeaderBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: LayoutMainHeaderBinding
    private var enabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        headerBinding = binding.lHeader
        headerBinding.tvTitle.text = getString(R.string.my_order)

        with(binding) {
            tvOrderTitle.text = getString(R.string.order_prepared)
            tvOrderETA.text = getString(R.string.order_eta)
            tvDriverName.text = getString(R.string.rider_name)
            tvRating.text = getString(R.string.rating)
            tvVehicleType.text = getString(R.string.vehicle_type)
            tvOrderSummary.text = getString(R.string.order_summary)
            tvCount.text = getString(R.string.number_of_orders)
            tvIcedLatte.text = getString(R.string.iced_latte)
            tvLarge.text = getString(R.string.large)
            tvAddOns.text = getString(R.string.hazelnut)
            tvPrice.text = getString(R.string.price)

            tvCountSecondOrder.text = getString(R.string.number_of_orders)
            tvIcedLatteSecondOrder.text = getString(R.string.iced_latte)
            tvLargeSecondOrder.text = getString(R.string.large)
            tvAddOnsSecondOrder.text = getString(R.string.hazelnut)
            tvPriceSecondOrder.text = getString(R.string.price)

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
}