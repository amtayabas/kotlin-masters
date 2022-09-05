package csv.masters.myapplication.data.repository

import csv.masters.myapplication.data.remote.dto.product.CoffeeResponse
import retrofit2.Response

interface ProductsRepository {

    suspend fun getProducts(): Response<CoffeeResponse>

}