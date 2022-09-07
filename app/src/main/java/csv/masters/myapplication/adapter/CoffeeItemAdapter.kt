package csv.masters.myapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import csv.masters.myapplication.R
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponseItem

class CoffeeItemAdapter : ListAdapter<CoffeeResponseItem, CoffeeItemAdapter.CoffeeItemViewHolder>(CoffeeItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeItemViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_name, parent, false)
        return CoffeeItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CoffeeItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CoffeeItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(coffeeResponseItem: CoffeeResponseItem) {
            val itemName : TextView = itemView.findViewById(R.id.tv_group_name)
            val products : RecyclerView = itemView.findViewById(R.id.recyclerView_product)
            products.layoutManager = LinearLayoutManager(itemView.context)
            object : LinearLayoutManager(itemView.context){ override fun canScrollVertically(): Boolean { return false } }
            itemName.text = coffeeResponseItem.name

            val productAdapter = CoffeeProductAdapter()
            products.adapter = productAdapter
            productAdapter.setOnItemClickListener(object : CoffeeProductAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val product = coffeeResponseItem.products[position]
                    Log.d("CoffeeItemAdapter", "onItemClick => $product")
                }
            })
            productAdapter.submitList(coffeeResponseItem.products)
        }
    }
}

class CoffeeItemDiffCallback : DiffUtil.ItemCallback<CoffeeResponseItem>() {

    override fun areItemsTheSame(oldItem: CoffeeResponseItem, newItem: CoffeeResponseItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CoffeeResponseItem, newItem: CoffeeResponseItem): Boolean {
        return oldItem == newItem
    }
}