package csv.masters.myapplication.data.repository

import csv.masters.myapplication.data.remote.api.ProductsApi
import csv.masters.myapplication.data.remote.dto.product.CoffeeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductsRepositoryImpl constructor(private val api: ProductsApi): ProductsRepository {

    override suspend fun getProducts(): Response<CoffeeResponse> {
        return withContext(Dispatchers.IO) {
            api.getProducts()
        }
    }

}