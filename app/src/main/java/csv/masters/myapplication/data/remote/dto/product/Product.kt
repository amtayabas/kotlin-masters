package csv.masters.myapplication.data.remote.dto.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("sizes")
    val sizes: String
)