package com.example.myapplication

import com.example.myapplication.data.model.BmkgResponse
import com.example.myapplication.data.model.Gempa
import com.example.myapplication.data.model.getMagnitudeValue
import com.example.myapplication.data.model.getSeverityCategory
import com.example.myapplication.data.model.isTsunamiWarning
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testBmkgJsonParsing() {
        val sampleJson = """
            {
              "Infogempa": {
                "gempa": [
                  {
                    "Tanggal": "06 Okt 2026",
                    "Jam": "20:11:23 WIB",
                    "DateTime": "2026-10-06T13:11:23+00:00",
                    "Coordinates": "4.94,118.77",
                    "Lintang": "4.94 LU",
                    "Bujur": "118.77 BT",
                    "Magnitude": "5.3",
                    "Kedalaman": "10 km",
                    "Wilayah": "219 km TimurLaut TARAKAN-KALTARA",
                    "Potensi": "Tidak berpotensi tsunami"
                  }
                ]
              }
            }
        """.trimIndent()

        val response = Gson().fromJson(sampleJson, BmkgResponse::class.java)
        val list = response.infogempa.gempa
        assertEquals(1, list.size)

        val gempa = list[0]
        assertEquals("5.3", gempa.magnitude)
        assertEquals(5.3, gempa.getMagnitudeValue(), 0.001)
        assertEquals("Sedang (Moderate)", gempa.getSeverityCategory())
        assertFalse(gempa.isTsunamiWarning())
        assertEquals("219 km TimurLaut TARAKAN-KALTARA", gempa.wilayah)
    }

    @Test
    fun testTsunamiWarningDetection() {
        val warningGempa = Gempa(
            magnitude = "7.2",
            potensi = "Peringatan Dini Tsunami untuk wilayah Maluku"
        )
        assertTrue(warningGempa.isTsunamiWarning())
        assertEquals("Sangat Kuat (Major)", warningGempa.getSeverityCategory())

        val safeGempa = Gempa(
            magnitude = "4.8",
            potensi = "Tidak berpotensi tsunami"
        )
        assertFalse(safeGempa.isTsunamiWarning())
        assertEquals("Ringan (Light)", safeGempa.getSeverityCategory())
    }
}