package csv.masters.myapplication.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.ItemProductBinding

class CoffeeItemAdapter : RecyclerView.Adapter<CoffeeItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

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
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val product = differ.currentList[position]

            with(holder.binding) {
                tvProductName.text = product.name
                tvSizes.text = product.sizes
                tvPrice.text = String.format("Php %.2f", product.price)

                Glide.with(this@apply)
                    .load(product.image)
                    .into(imgProduct)
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