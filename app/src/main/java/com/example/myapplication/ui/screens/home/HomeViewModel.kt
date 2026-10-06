package com.example.myapplication.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Gempa
import com.example.myapplication.data.model.GempaUiState
import com.example.myapplication.data.repository.GempaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola state katalog dan pemantauan gempa BMKG.
 * Mengimplementasikan arsitektur MVVM dengan StateFlow (pola P5).
 */
class HomeViewModel(
    private val repository: GempaRepository = GempaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<GempaUiState>(GempaUiState.Loading)
    val uiState: StateFlow<GempaUiState> = _uiState.asStateFlow()

    private var rawGempaList: List<Gempa> = emptyList()
    private var currentSearchQuery: String = ""

    init {
        fetchGempa()
    }

    fun fetchGempa() {
        viewModelScope.launch {
            _uiState.value = GempaUiState.Loading
            val result = repository.getGempaTerkini()
            result.onSuccess { list ->
                rawGempaList = list
                applySearch(currentSearchQuery)
            }.onFailure { throwable ->
                _uiState.value = GempaUiState.Error(
                    throwable.localizedMessage ?: "Terjadi kesalahan saat memuat data gempa BMKG."
                )
            }
        }
    }

    fun searchGempa(query: String) {
        currentSearchQuery = query
        applySearch(query)
    }

    private fun applySearch(query: String) {
        val trimmed = query.trim()
        val filtered = if (trimmed.isEmpty()) {
            rawGempaList
        } else {
            rawGempaList.filter { gempa ->
                gempa.wilayah.contains(trimmed, ignoreCase = true) ||
                        gempa.potensi.contains(trimmed, ignoreCase = true) ||
                        gempa.tanggal.contains(trimmed, ignoreCase = true)
            }
        }
        _uiState.value = GempaUiState.Success(
            gempaList = rawGempaList,
            filteredList = filtered,
            searchQuery = currentSearchQuery
        )
    }

    fun getGempaByIndex(index: Int): Gempa? {
        return rawGempaList.getOrNull(index)
    }
}
