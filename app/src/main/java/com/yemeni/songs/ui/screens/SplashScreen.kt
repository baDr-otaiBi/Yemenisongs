package com.yemeni.songs.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yemeni.songs.ui.theme.DeepRed
import com.yemeni.songs.ui.theme.GoldAccent
import com.yemeni.songs.ui.theme.PrimaryGreen
import com.yemeni.songs.ui.theme.WarmGold
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onEnterClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    val iconScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.3f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "iconScale"
    )

    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
        delay(600)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PrimaryGreen,
                        PrimaryGreen.copy(alpha = 0.85f),
                        Color(0xFF004D1A),
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon with animation
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(iconScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                WarmGold,
                                GoldAccent,
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(600)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "أغاني يمنية",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "بدون نت",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                        color = WarmGold,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "استمتع بأجمل الأغاني اليمنية الأصيلة",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Enter Button
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(tween(500)) + slideInVertically(
                    initialOffsetY = { 80 },
                    animationSpec = tween(500)
                )
            ) {
                Button(
                    onClick = onEnterClick,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmGold,
                        contentColor = PrimaryGreen,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp,
                    )
                ) {
                    Text(
                        text = "دخول",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Decorative dots
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(tween(800, delayMillis = 200))
            ) {
                Text(
                    text = "♪ ♫ ♪ ♫ ♪",
                    style = MaterialTheme.typography.titleLarge,
                    color = WarmGold.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    letterSpacing = 8.sp,
                )
            }
        }
    }
}
