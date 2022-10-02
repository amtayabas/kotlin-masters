package csv.masters.myapplication.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

class DataStoreManager constructor(private val context: Context) {

    suspend fun putString(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun <T> putObject(key: String, obj: Any, classOfT: Class<T>) {
        putString(key, Gson().toJson(obj, classOfT))
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun getString(key: String): String? {
        return context.dataStore.data.first()[stringPreferencesKey(key)]
    }

    suspend fun <T> getObject(key: String, classOfT: Class<T>): T? {
        val json = getString(key)
        return Gson().fromJson(json, classOfT) ?: return null
    }

    suspend fun <T> getObjectList(key: String, classOfT: Class<T>): ArrayList<T>? {
        val json = getString(key)
        return Gson().fromJson(json, TypeToken.getParameterized(ArrayList::class.java, classOfT).type)
    }

    suspend fun getBoolean(key: String): Boolean? {
        return context.dataStore.data.first()[booleanPreferencesKey(key)]
    }

    suspend fun deleteData(key: String) {
        context.dataStore.edit {
            if (it.contains(stringPreferencesKey(key))) {
                it.remove(stringPreferencesKey(key))
            }
        }
    }

    suspend fun clearData() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private const val DATASTORE_NAME = "kotlin_masters_datastore"
        private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)
    }

}