package csv.masters.myapplication.data.local

import csv.masters.myapplication.common.Constants
import csv.masters.myapplication.data.remote.dto.product.Product

class OrderManager constructor(private val dataStoreManager: DataStoreManager) {

    inner class Operations {
        suspend fun getUpcomingOrders(): ArrayList<Product> {
            return dataStoreManager.getObjectList(Constants.Order.UPCOMING_ORDER, Product::class.java) ?: arrayListOf()
        }

        suspend fun emptyUpcomingOrders() {
            dataStoreManager.deleteData(Constants.Order.UPCOMING_ORDER)
        }

        suspend fun addUpcomingOrders(basketManager: BasketManager) {
            val upcomingOrder = basketManager.Operations().getBasket()
            dataStoreManager.putObject(Constants.Order.UPCOMING_ORDER, upcomingOrder, ArrayList::class.java)
            basketManager.Operations().emptyBasket()
        }

    }

}