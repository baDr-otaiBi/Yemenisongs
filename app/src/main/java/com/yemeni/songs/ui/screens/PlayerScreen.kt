package com.yemeni.songs.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yemeni.songs.data.SingerTheme
import com.yemeni.songs.data.SingerThemes
import com.yemeni.songs.player.MusicPlayer
import com.yemeni.songs.player.formatTime

@Composable
fun PlayerScreen(
    musicPlayer: MusicPlayer,
    onDismiss: () -> Unit
) {
    val song = musicPlayer.currentSong ?: return
    val singer = musicPlayer.currentSinger ?: return
    val theme = SingerThemes.getThemeForSinger(singer.id)

    // Vinyl rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vinylRotation"
    )

    // Pulse animation for play button
    val pulseScale by animateFloatAsState(
        targetValue = if (musicPlayer.isPlaying) 1.05f else 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "pulse"
    )

    // Background glow animation
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        theme.gradientStart,
                        theme.gradientEnd,
                        Color.Black.copy(alpha = 0.95f),
                    ),
                    startY = 0f,
                    endY = 2000f,
                )
            )
    ) {
        // Animated background glow circles
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopEnd)
                .blur(100.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            theme.accent.copy(alpha = glowAlpha * 0.3f),
                            Color.Transparent,
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomStart)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            theme.primary.copy(alpha = glowAlpha * 0.4f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "إغلاق",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp),
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "يعمل الآن",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.7f),
                    )
                    Text(
                        text = singer.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = theme.accent,
                        fontWeight = FontWeight.Bold,
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                        contentDescription = "القائمة",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Album Art / Vinyl disc
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                        ambientColor = theme.accent.copy(alpha = 0.3f),
                        spotColor = theme.accent.copy(alpha = 0.3f),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                // Outer vinyl ring
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .rotate(if (musicPlayer.isPlaying) rotation else 0f)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFF1A1A1A),
                                    Color(0xFF2D2D2D),
                                    Color(0xFF1A1A1A),
                                    Color(0xFF333333),
                                    Color(0xFF1A1A1A),
                                    Color(0xFF2D2D2D),
                                    Color(0xFF1A1A1A),
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    // Vinyl grooves
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF2A2A2A),
                                        Color(0xFF1E1E1E),
                                        Color(0xFF2A2A2A),
                                        Color(0xFF1E1E1E),
                                        Color(0xFF2A2A2A),
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Center label
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            theme.accent,
                                            theme.primary,
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = singer.emoji,
                                    fontSize = 32.sp,
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "♪",
                                    fontSize = 16.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                )
                            }
                        }
                    }
                }

                // Center hole
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0A0A0A))
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Song title and singer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = singer.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = theme.accent.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Progress bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = musicPlayer.progress,
                    onValueChange = { musicPlayer.seekTo(it) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = theme.accent,
                        activeTrackColor = theme.accent,
                        inactiveTrackColor = Color.White.copy(alpha = 0.15f),
                    ),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = formatTime(musicPlayer.currentPosition),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                    )
                    Text(
                        text = formatTime(musicPlayer.totalDuration),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Shuffle
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "خلط",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp),
                    )
                }

                // Previous
                IconButton(
                    onClick = { musicPlayer.playPrevious() },
                    modifier = Modifier.size(56.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "السابق",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp),
                    )
                }

                // Play/Pause (big button)
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(pulseScale)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            ambientColor = theme.accent.copy(alpha = 0.4f),
                            spotColor = theme.accent.copy(alpha = 0.4f),
                        )
                        .clip(CircleShape)
                        .background(theme.accent),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(
                        onClick = { musicPlayer.togglePlayPause() },
                        modifier = Modifier.size(72.dp),
                    ) {
                        AnimatedVisibility(
                            visible = musicPlayer.isPlaying,
                            enter = fadeIn(tween(200)),
                            exit = fadeOut(tween(200)),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Pause,
                                contentDescription = "إيقاف",
                                tint = theme.primaryDark,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                        AnimatedVisibility(
                            visible = !musicPlayer.isPlaying,
                            enter = fadeIn(tween(200)),
                            exit = fadeOut(tween(200)),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "تشغيل",
                                tint = theme.primaryDark,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    }
                }

                // Next
                IconButton(
                    onClick = { musicPlayer.playNext() },
                    modifier = Modifier.size(56.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "التالي",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp),
                    )
                }

                // Repeat
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "تكرار",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Audio wave visualizer (decorative)
            AnimatedVisibility(visible = musicPlayer.isPlaying) {
                AudioWaveVisualizer(theme = theme)
            }
        }
    }
}

@Composable
fun AudioWaveVisualizer(theme: SingerTheme) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val barCount = 20
        for (i in 0 until barCount) {
            val barHeight by infiniteTransition.animateFloat(
                initialValue = 4f,
                targetValue = when {
                    i % 3 == 0 -> 28f
                    i % 2 == 0 -> 20f
                    else -> 14f
                },
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600 + (i * 50),
                        easing = FastOutSlowInEasing,
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "bar_$i"
            )

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(barHeight.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                theme.accent,
                                theme.accent.copy(alpha = 0.4f),
                            )
                        )
                    )
            )
        }
    }
}
