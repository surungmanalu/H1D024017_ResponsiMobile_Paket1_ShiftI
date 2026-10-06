package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.data.model.GempaUiState
import com.example.myapplication.ui.components.EmptyView
import com.example.myapplication.ui.components.ErrorView
import com.example.myapplication.ui.components.GempaCard
import com.example.myapplication.ui.components.GempaSearchBar
import com.example.myapplication.ui.components.LoadingView

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Header Bergaya Pokédex: Cobalt Blue dengan kurva bawah membulat
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                color = Color(0xFF1E3A8A), // Deep Cobalt Blue khas Pokédex
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = innerPadding.calculateTopPadding() + 8.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                ) {
                    // Baris Atas: Ikon Logo BMKG Bulat + Judul + Tombol Refresh
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Logo BMKG Bulat tanpa teks luar (Emblem Segini Doang)
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_bmkg_logo),
                                    contentDescription = "BMKG Logo",
                                    modifier = Modifier.size(38.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "BMKG QuakeWatch",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.25.sp
                            )
                        }

                        // Tombol Aksi Kanan (Circular elevated button khas Pokédex)
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.size(42.dp)
                        ) {
                            IconButton(onClick = { viewModel.fetchGempa() }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Muat Ulang Data",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar 1 Baris Elegan di dalam Header Biru
                    val currentQuery = (uiState as? GempaUiState.Success)?.searchQuery ?: ""
                    GempaSearchBar(
                        query = currentQuery,
                        onQueryChange = { query -> viewModel.searchGempa(query) }
                    )
                }
            }

            // Body List: LazyColumn dengan format grid 2 kolom bernuansa Pokédex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (val state = uiState) {
                    is GempaUiState.Loading -> {
                        LoadingView()
                    }

                    is GempaUiState.Error -> {
                        ErrorView(
                            message = state.message,
                            onRetry = { viewModel.fetchGempa() }
                        )
                    }

                    is GempaUiState.Success -> {
                        if (state.filteredList.isEmpty()) {
                            EmptyView(searchQuery = state.searchQuery)
                        } else {
                            // Chunked into pairs for 2-column Pokédex layout via LazyColumn
                            val chunkedList = state.filteredList.chunked(2)

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                itemsIndexed(
                                    items = chunkedList,
                                    key = { rowIndex, _ -> "row-$rowIndex" }
                                ) { rowIndex, pair ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                                    ) {
                                        // Kartu Pertama
                                        val firstGempa = pair[0]
                                        val firstOriginalIndex = state.gempaList.indexOf(firstGempa).let {
                                            if (it >= 0) it else rowIndex * 2
                                        }
                                        GempaCard(
                                            gempa = firstGempa,
                                            index = rowIndex * 2,
                                            onClick = { onNavigateToDetail(firstOriginalIndex) },
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Kartu Kedua (jika genap)
                                        if (pair.size > 1) {
                                            val secondGempa = pair[1]
                                            val secondOriginalIndex = state.gempaList.indexOf(secondGempa).let {
                                                if (it >= 0) it else rowIndex * 2 + 1
                                            }
                                            GempaCard(
                                                gempa = secondGempa,
                                                index = rowIndex * 2 + 1,
                                                onClick = { onNavigateToDetail(secondOriginalIndex) },
                                                modifier = Modifier.weight(1f)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
