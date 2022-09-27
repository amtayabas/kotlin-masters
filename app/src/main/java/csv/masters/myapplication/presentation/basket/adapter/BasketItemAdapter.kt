package csv.masters.myapplication.presentation.basket.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import csv.masters.myapplication.R
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.ItemBasketProductBinding
import csv.masters.myapplication.presentation.basket.BasketFragmentDirections

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBasketProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val product = differ.currentList[position]

            with(holder.binding) {
                tvProductName.text = product.name
                tvSizes.text = context.getString(R.string.sizes)
                tvAddOns.text = context.getString(R.string.add_ons)
                tvPrice.text = String.format("Php %.2f", product.totalProductPrice)
                tvQuantity.text = product.quantity.toString()

                tvEdit.text = context.getString(R.string.edit)
                tvEdit.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }

            holder.binding.tvEdit.setOnClickListener {
                findNavController().navigate(BasketFragmentDirections.actionBasketFragmentToProductDetailFragment(product))
                onItemClickListener?.let { it(product) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }
}