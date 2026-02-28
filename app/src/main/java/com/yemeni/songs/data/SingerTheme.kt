package com.yemeni.songs.data

import androidx.compose.ui.graphics.Color

data class SingerTheme(
    val primary: Color,
    val primaryDark: Color,
    val accent: Color,
    val gradientStart: Color,
    val gradientEnd: Color,
    val cardBackground: Color,
    val textOnGradient: Color = Color.White,
)

object SingerThemes {
    // أيوب طارش - أخضر وذهبي (ألوان العلم اليمني)
    val ayoubTarish = SingerTheme(
        primary = Color(0xFF1B5E20),
        primaryDark = Color(0xFF003300),
        accent = Color(0xFFF5C842),
        gradientStart = Color(0xFF1B5E20),
        gradientEnd = Color(0xFF004D1A),
        cardBackground = Color(0xFF1B5E20).copy(alpha = 0.08f),
    )

    // أبو بكر سالم - أزرق داكن وفضي (أناقة حضرمية)
    val abuBakrSalem = SingerTheme(
        primary = Color(0xFF1A237E),
        primaryDark = Color(0xFF0D1642),
        accent = Color(0xFFC0C0C0),
        gradientStart = Color(0xFF1A237E),
        gradientEnd = Color(0xFF0D47A1),
        cardBackground = Color(0xFF1A237E).copy(alpha = 0.08f),
    )

    // فيصل علوي - أحمر دافئ وذهبي (دفء عدن)
    val faisalAlawi = SingerTheme(
        primary = Color(0xFFB71C1C),
        primaryDark = Color(0xFF7F0000),
        accent = Color(0xFFFFD700),
        gradientStart = Color(0xFFB71C1C),
        gradientEnd = Color(0xFF880E0E),
        cardBackground = Color(0xFFB71C1C).copy(alpha = 0.08f),
    )

    // محمد حمود الحارثي - بنفسجي وذهبي (ملكي صنعاني)
    val mohammedHamoud = SingerTheme(
        primary = Color(0xFF4A148C),
        primaryDark = Color(0xFF2C0052),
        accent = Color(0xFFFFAB40),
        gradientStart = Color(0xFF4A148C),
        gradientEnd = Color(0xFF6A1B9A),
        cardBackground = Color(0xFF4A148C).copy(alpha = 0.08f),
    )

    // علي بن علي الآنسي - تركوازي وعنبري (تراثي أصيل)
    val aliBinAli = SingerTheme(
        primary = Color(0xFF00695C),
        primaryDark = Color(0xFF003D33),
        accent = Color(0xFFFFB300),
        gradientStart = Color(0xFF00695C),
        gradientEnd = Color(0xFF00897B),
        cardBackground = Color(0xFF00695C).copy(alpha = 0.08f),
    )

    fun getThemeForSinger(singerId: Int): SingerTheme = when (singerId) {
        1 -> ayoubTarish
        2 -> abuBakrSalem
        3 -> faisalAlawi
        4 -> mohammedHamoud
        5 -> aliBinAli
        else -> ayoubTarish
    }
}
