package csv.masters.myapplication.data.remote.dto.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val sizes: String?,

    var size: String? = "Regular (12oz)",
    var addOn: ArrayList<String>? = arrayListOf(),
    var itemInBasket: Int = 0,
    var totalProductPrice: Float,
    var quantity: Int = 1,
): Parcelable