package csv.masters.myapplication.data.local

import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.remote.dto.product.Product

class BasketManager constructor(private val dataStoreManager: DataStoreManager) {

    inner class Operations {
        suspend fun getBasket(): ArrayList<Product> {
            return dataStoreManager.getObjectList(Constants.BASKET_OBJECT, Product::class.java) ?: arrayListOf()
        }

        suspend fun emptyBasket() {
            dataStoreManager.deleteData(Constants.BASKET_OBJECT)
        }

        suspend fun addToBasket(product: Product): ArrayList<Product> {
            val basket = getBasket()
            val productInBasket =
                basket.firstOrNull { prod -> prod.name == product.name && prod.description == product.description }
            if (basket.isEmpty() || productInBasket == null) {
                product.itemInBasket++
            } else {
                product.itemInBasket = productInBasket.itemInBasket + 1
            }
            basket.remove(productInBasket)
            basket.add(product)
            dataStoreManager.putObject(Constants.BASKET_OBJECT, basket, ArrayList::class.java)
            return basket
        }

        suspend fun removeFromBasket(product: Product): ArrayList<Product> {
            val basket = getBasket()
            val productInBasket =
                basket.firstOrNull { prod -> prod.name == product.name && prod.description == product.description }
            if (basket.isNotEmpty() || productInBasket != null) {
                product.itemInBasket = productInBasket!!.itemInBasket - 1
                basket.remove(product)
                if (product.itemInBasket > 0) {
                    basket.add(product)
                }
                dataStoreManager.putObject(Constants.BASKET_OBJECT, basket, ArrayList::class.java)
            }
            return basket
        }

        suspend fun updateFromBasket(product: Product): ArrayList<Product> {
            val basket = getBasket()
            val productInBasket =
                basket.firstOrNull { prod -> prod.name == product.name && prod.description == product.description }
            if (isProductInBasket(product)) {
                if (productInBasket != null) {
                    basket.set(basket.indexOf(productInBasket), product)
                }
            }

            dataStoreManager.putObject(Constants.BASKET_OBJECT, basket, ArrayList::class.java)
            return basket
        }

        private suspend fun isProductInBasket(product: Product): Boolean {
            val basket = getBasket()
            return (basket.isNotEmpty() && basket.find { prod -> prod.name == product.name && prod.description == product.description } != null)
        }
    }

}