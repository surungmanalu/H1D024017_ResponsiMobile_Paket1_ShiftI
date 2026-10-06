package com.example.myapplication.data.model

/**
 * Sealed interface untuk merepresentasikan state UI sesuai arsitektur MVVM & pola P5.
 */
sealed interface GempaUiState {
    data object Loading : GempaUiState

    data class Success(
        val gempaList: List<Gempa>,
        val filteredList: List<Gempa> = gempaList,
        val searchQuery: String = ""
    ) : GempaUiState

    data class Error(
        val message: String
    ) : GempaUiState
}
