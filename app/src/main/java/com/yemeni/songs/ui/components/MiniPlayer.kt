package com.yemeni.songs.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yemeni.songs.data.SingerThemes
import com.yemeni.songs.player.MusicPlayer

@Composable
fun MiniPlayer(
    musicPlayer: MusicPlayer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val song = musicPlayer.currentSong ?: return
    val singer = musicPlayer.currentSinger ?: return
    val theme = SingerThemes.getThemeForSinger(singer.id)

    val infiniteTransition = rememberInfiniteTransition(label = "miniVinyl")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "miniRotation"
    )

    AnimatedVisibility(
        visible = musicPlayer.hasSong,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier,
    ) {
        Column {
            // Progress bar on top
            LinearProgressIndicator(
                progress = { musicPlayer.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = theme.accent,
                trackColor = Color.Transparent,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                    )
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                theme.gradientStart.copy(alpha = 0.95f),
                                theme.gradientEnd.copy(alpha = 0.95f),
                            )
                        )
                    )
                    .clickable(onClick = onClick)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Mini vinyl
                Box(
                    modifier = Modifier
                        .size(44.dp)
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
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(theme.accent, theme.primary)
                                )
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = singer.emoji, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Song info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = singer.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.accent.copy(alpha = 0.8f),
                        maxLines = 1,
                    )
                }

                // Mini wave animation
                if (musicPlayer.isPlaying) {
                    MiniWave(theme.accent)
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Play/Pause
                IconButton(
                    onClick = { musicPlayer.togglePlayPause() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(theme.accent),
                ) {
                    Icon(
                        imageVector = if (musicPlayer.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (musicPlayer.isPlaying) "إيقاف" else "تشغيل",
                        tint = theme.primaryDark,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Next
                IconButton(
                    onClick = { musicPlayer.playNext() },
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "التالي",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun MiniWave(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "miniWave")

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 0..2) {
            val height by infiniteTransition.animateFloat(
                initialValue = 6f,
                targetValue = 18f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 400 + (i * 150),
                        easing = FastOutSlowInEasing,
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "miniBar_$i"
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(color.copy(alpha = 0.8f))
            )
        }
    }
}
