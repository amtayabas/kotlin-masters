package csv.masters.myapplication.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import csv.masters.myapplication.R
import csv.masters.myapplication.data.remote.dto.product.Product

class CoffeeProductAdapter : ListAdapter<Product, CoffeeProductAdapter.CoffeeProductViewHolder>(CoffeeProductDiffCallback()) {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeProductViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return CoffeeProductViewHolder(layout, mListener)
    }

    override fun onBindViewHolder(holder: CoffeeProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CoffeeProductViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }

        fun bind(product: Product) {
            val productImage : ImageView = itemView.findViewById(R.id.imgProduct)
            val productSize : TextView = itemView.findViewById(R.id.tv_sizes)
            val productName : TextView = itemView.findViewById(R.id.tv_product_name)
            val productPrice : TextView = itemView.findViewById(R.id.tv_price)

            Glide.with(itemView.context).load(product.image).into(productImage)
            productSize.text = product.sizes
            productName.text = product.name
            productPrice.text = itemView.context.getString(R.string.product_price, product.price.toString())
        }
    }
}

class CoffeeProductDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.price == newItem.price
    }
}