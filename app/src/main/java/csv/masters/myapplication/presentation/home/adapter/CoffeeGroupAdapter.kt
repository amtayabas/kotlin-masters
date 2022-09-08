package csv.masters.myapplication.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponseItem
import csv.masters.myapplication.data.remote.dto.product.Product
import csv.masters.myapplication.databinding.ItemProductGroupBinding

class CoffeeGroupAdapter :
    RecyclerView.Adapter<CoffeeGroupAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemProductGroupBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CoffeeResponseItem>() {
        override fun areItemsTheSame(oldItem: CoffeeResponseItem, newItem: CoffeeResponseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CoffeeResponseItem, newItem: CoffeeResponseItem): Boolean {
            return oldItem.products == newItem.products
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val coffeeGroup = differ.currentList[position]

            with(holder.binding) {
                tvGroupName.text = coffeeGroup.name

                val productAdapter = CoffeeItemAdapter()
                recyclerViewProduct.apply {
                    adapter = productAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }

                onItemClickListener?.let {
                    productAdapter.setOnItemClickListener(it)
                }

                productAdapter.differ.submitList(coffeeGroup.products)
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