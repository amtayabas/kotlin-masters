package csv.masters.myapplication.presentation.basket.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import csv.masters.myapplication.R
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.ItemBasketProductBinding

class BasketItemAdapter : RecyclerView.Adapter<BasketItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBasketProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.price == newItem.price
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    var isOrderOnTheWay: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBasketProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setIsOrderOnTheWay(orderOnTheWay: Boolean) {
        isOrderOnTheWay = orderOnTheWay
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val product = differ.currentList[position]

            with(holder.binding) {
                tvProductName.text = product.name
                tvSizes.text = product.size
                var addOn = ""
                product.addOn?.let {
                    it.forEach { item ->
                        addOn += "$item "
                    }
                }
                if (addOn.isNotEmpty()) {
                    tvAddOns.visibility = View.VISIBLE
                    tvAddOns.text = addOn
                }
                tvPrice.text = String.format("Php %.2f", product.totalProductPrice)
                tvQuantity.text = product.quantity.toString()

                if (!isOrderOnTheWay) {
                    tvEdit.visibility = View.VISIBLE
                    tvEdit.text = context.getString(R.string.edit)
                    tvEdit.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
            }

            holder.binding.tvEdit.setOnClickListener {
                onItemEditClickListener?.let { it(product) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemEditClickListener: ((Product) -> Unit)? = null

    fun setOnItemEditClickListener(listener: (Product) -> Unit) {
        onItemEditClickListener = listener
    }
}