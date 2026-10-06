package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.Gempa
import com.example.myapplication.data.model.getMagnitudeValue
import com.example.myapplication.data.model.isTsunamiWarning

/**
 * Mendapatkan warna adaptif kartu berdasarkan tingkat keparahan / bahaya gempa
 * terinspirasi oleh palet warna elemen Pokédex.
 */
fun getGempaSeverityColor(gempa: Gempa): Color {
    val mag = gempa.getMagnitudeValue()
    return when {
        gempa.isTsunamiWarning() || mag >= 6.3 -> Color(0xFFE55039) // Bahaya / Tsunami (Crimson Alert)
        mag >= 5.6 -> Color(0xFFE58E26)                             // Kuat / Siaga (Magma Orange)
        mag >= 5.0 -> Color(0xFF4A9DA9)                             // Sedang / Waspada (Oceanic Teal)
        else -> Color(0xFF58B48F)                                   // Ringan / Aman (Mint Green)
    }
}

fun getGempaSeverityLabel(gempa: Gempa): String {
    val mag = gempa.getMagnitudeValue()
    return when {
        gempa.isTsunamiWarning() -> "TSUNAMI"
        mag >= 6.3 -> "Sangat Kuat"
        mag >= 5.6 -> "Kuat"
        mag >= 5.0 -> "Sedang"
        else -> "Ringan"
    }
}

/**
 * Kartu Gempa dengan gaya visual Pokédex:
 * - Sudut membulat modern (RoundedCornerShape 24.dp)
 * - Warna background adaptif sesuai tingkat bahaya gempa
 * - Chip badge di kiri atas (Severity / Magnitudo)
 * - Kedalaman di kanan atas (#001 / 10 km)
 * - Lingkaran visual di tengah dengan angka Magnitudo
 * - Judul Wilayah tebal di bagian bawah
 */
@Composable
fun GempaCard(
    gempa: Gempa,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBgColor = getGempaSeverityColor(gempa)
    val severityLabel = getGempaSeverityLabel(gempa)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Baris Atas: Badge Tingkat Bahaya (Kiri) dan Kedalaman / No Index (Kanan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge Kiri Atas (Translucent Pill)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.28f)
                ) {
                    Text(
                        text = severityLabel,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Info Kanan Atas (#Index atau Kedalaman)
                Text(
                    text = String.format("#%03d", index + 1),
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Lingkaran Tengah: Visual Magnitudo & Gelombang Seismik
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = gempa.magnitude.ifEmpty { "-" },
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (gempa.isTsunamiWarning()) Icons.Default.Warning else Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = " SR",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Nama Wilayah (Bold White Text bergaya Pokédex)
            Text(
                text = gempa.wilayah,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Info Tanggal & Kedalaman Singkat
            Text(
                text = "${gempa.tanggal} • ${gempa.kedalaman}",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
