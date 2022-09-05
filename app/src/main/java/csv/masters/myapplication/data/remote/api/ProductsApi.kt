package csv.masters.myapplication.data.remote.api

import csv.masters.myapplication.data.remote.dto.product.CoffeeResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApi {

    @GET("products")
    suspend fun getProducts(): Response<CoffeeResponse>

}