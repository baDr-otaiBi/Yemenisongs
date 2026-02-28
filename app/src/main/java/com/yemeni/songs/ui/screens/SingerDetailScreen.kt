package com.yemeni.songs.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yemeni.songs.data.Singer
import com.yemeni.songs.data.SingerTheme
import com.yemeni.songs.data.SingerThemes
import com.yemeni.songs.data.SingersData
import com.yemeni.songs.data.Song
import com.yemeni.songs.player.MusicPlayer
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingerDetailScreen(
    singerId: Int,
    musicPlayer: MusicPlayer,
    onBackClick: () -> Unit,
    onSongClick: (Song, Singer, List<Song>, Int) -> Unit,
) {
    val singer = SingersData.getSingerById(singerId) ?: return
    val theme = SingerThemes.getThemeForSinger(singerId)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    // Track which song we're importing for
    var importingSongId by remember { mutableIntStateOf(-1) }
    // Counter to force recomposition after import
    var importVersion by remember { mutableIntStateOf(0) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && importingSongId > 0) {
            val success = musicPlayer.importSong(singerId, importingSongId, uri)
            if (success) {
                importVersion++
                Toast.makeText(context, "تم إضافة الأغنية بنجاح ✓", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "فشل في إضافة الأغنية", Toast.LENGTH_SHORT).show()
            }
        }
        importingSongId = -1
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = singer.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "رجوع",
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Singer header card with theme colors
            item {
                SingerHeaderCard(
                    singer = singer,
                    theme = theme,
                    savedCount = musicPlayer.songStorage.getSavedSongCount(singerId),
                    importVersion = importVersion,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Play all button
            item {
                PlayAllButton(theme = theme) {
                    if (singer.songs.isNotEmpty()) {
                        onSongClick(singer.songs[0], singer, singer.songs, 0)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Songs list
            itemsIndexed(singer.songs) { index, song ->
                var isVisible by remember { mutableStateOf(false) }
                val isCurrentSong = musicPlayer.currentSong?.id == song.id &&
                        musicPlayer.currentSinger?.id == singer.id
                // Check if song has been imported (use importVersion to react to changes)
                val hasSavedFile = remember(importVersion) {
                    musicPlayer.songStorage.hasSong(singerId, song.id)
                }

                LaunchedEffect(Unit) {
                    delay(index * 80L)
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(300)) + slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = tween(300)
                    )
                ) {
                    SongItem(
                        song = song,
                        index = index + 1,
                        theme = theme,
                        isPlaying = isCurrentSong && musicPlayer.isPlaying,
                        isCurrentSong = isCurrentSong,
                        hasSavedFile = hasSavedFile,
                        onClick = { onSongClick(song, singer, singer.songs, index) },
                        onImportClick = {
                            importingSongId = song.id
                            filePickerLauncher.launch("audio/*")
                        },
                    )
                }
            }

            // Bottom spacer for mini player
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun PlayAllButton(theme: SingerTheme, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.cardBackground),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(theme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "تشغيل الكل",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "تشغيل الكل",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = theme.primary,
            )
        }
    }
}

@Composable
fun SingerHeaderCard(
    singer: Singer,
    theme: SingerTheme,
    savedCount: Int,
    @Suppress("UNUSED_PARAMETER") importVersion: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            theme.gradientStart,
                            theme.gradientEnd,
                        )
                    )
                )
                .padding(24.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    theme.accent,
                                    theme.accent.copy(alpha = 0.6f),
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = singer.emoji,
                        fontSize = 40.sp,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = singer.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = singer.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${singer.songsCount} أغنية",
                                style = MaterialTheme.typography.labelLarge,
                                color = theme.accent,
                            )
                        }
                        if (savedCount > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "$savedCount محفوظة",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(alpha = 0.9f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongItem(
    song: Song,
    index: Int,
    theme: SingerTheme,
    isPlaying: Boolean,
    isCurrentSong: Boolean,
    hasSavedFile: Boolean,
    onClick: () -> Unit,
    onImportClick: () -> Unit,
) {
    val cardBg = if (isCurrentSong)
        theme.primary.copy(alpha = 0.12f)
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentSong) 4.dp else 1.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Song number / playing indicator
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCurrentSong)
                            Brush.linearGradient(listOf(theme.primary, theme.gradientEnd))
                        else
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.primaryContainer,
                                )
                            )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPlaying) {
                    MiniSongWave(color = Color.White)
                } else {
                    Text(
                        text = "$index",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = if (isCurrentSong) Color.White
                        else MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Song info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (isCurrentSong) FontWeight.Bold else FontWeight.Medium,
                        ),
                        color = if (isCurrentSong) theme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (hasSavedFile) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "محفوظة",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4CAF50),
                        )
                    }
                }
                if (song.duration.isNotEmpty()) {
                    Text(
                        text = if (hasSavedFile) "${song.duration} • جاهزة للتشغيل"
                        else song.duration,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isCurrentSong) theme.primary.copy(alpha = 0.7f)
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Import button (if not imported)
            if (!hasSavedFile) {
                IconButton(
                    onClick = onImportClick,
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.FileUpload,
                        contentDescription = "استيراد",
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
                }
            }

            // Play/Pause icon
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCurrentSong) theme.accent.copy(alpha = 0.2f)
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayCircle,
                    contentDescription = "تشغيل",
                    modifier = Modifier.size(if (isPlaying) 24.dp else 32.dp),
                    tint = if (isCurrentSong) theme.primary
                    else MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun MiniSongWave(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "songWave")
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 0..2) {
            val height by infiniteTransition.animateFloat(
                initialValue = 4f,
                targetValue = 16f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 350 + (i * 120),
                        easing = FastOutSlowInEasing,
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "songBar_$i"
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(color)
            )
        }
    }
}
