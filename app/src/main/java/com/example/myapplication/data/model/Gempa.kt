package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model data gempa BMKG berdasarkan spesifikasi endpoint:
 * https://data.bmkg.go.id/DataMKG/TEWS/gempaterkini.json
 */
data class Gempa(
    @SerializedName("Tanggal")
    val tanggal: String = "",

    @SerializedName("Jam")
    val jam: String = "",

    @SerializedName("DateTime")
    val dateTime: String? = null,

    @SerializedName("Coordinates")
    val coordinates: String = "",

    @SerializedName("Lintang")
    val lintang: String? = null,

    @SerializedName("Bujur")
    val bujur: String? = null,

    @SerializedName("Magnitude")
    val magnitude: String = "",

    @SerializedName("Kedalaman")
    val kedalaman: String = "",

    @SerializedName("Wilayah")
    val wilayah: String = "",

    @SerializedName("Potensi")
    val potensi: String = ""
)

/**
 * Extension functions untuk mempermudah manipulasi dan penyajian data gempa.
 */
fun Gempa.getMagnitudeValue(): Double {
    return magnitude.toDoubleOrNull() ?: 0.0
}

fun Gempa.getSeverityCategory(): String {
    val mag = getMagnitudeValue()
    return when {
        mag >= 7.0 -> "Sangat Kuat (Major)"
        mag >= 6.0 -> "Kuat (Strong)"
        mag >= 5.0 -> "Sedang (Moderate)"
        else -> "Ringan (Light)"
    }
}

fun Gempa.isTsunamiWarning(): Boolean {
    val lower = potensi.lowercase()
    return lower.contains("tsunami") && !lower.contains("tidak berpotensi")
}
