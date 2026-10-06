package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Root wrapper response dari API BMKG:
 * { "Infogempa": { "gempa": [ ... ] } }
 */
data class BmkgResponse(
    @SerializedName("Infogempa")
    val infogempa: InfoGempaContainer
)

data class InfoGempaContainer(
    @SerializedName("gempa")
    val gempa: List<Gempa> = emptyList()
)
