package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Gempa
import com.example.myapplication.data.remote.BmkgApiService
import com.example.myapplication.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository layer untuk abstraksi data gempa BMKG.
 * Mengisolasi pemanggilan jaringan dari Composable dan ViewModel.
 */
class GempaRepository(
    private val apiService: BmkgApiService = RetrofitClient.apiService
) {
    suspend fun getGempaTerkini(): Result<List<Gempa>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getGempaTerkini()
                val list = response.infogempa.gempa
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
