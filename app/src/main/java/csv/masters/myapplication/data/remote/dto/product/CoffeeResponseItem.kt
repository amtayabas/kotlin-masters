package csv.masters.myapplication.data.remote.dto.product

import com.google.gson.annotations.SerializedName

data class CoffeeResponseItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("products")
    val products: List<Product>
)