package com.yemeni.songs.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.ui.graphics.vector.ImageVector

data class Singer(
    val id: Int,
    val name: String,
    val description: String,
    val songsCount: Int,
    val emoji: String,
    val songs: List<Song> = emptyList()
)

data class Song(
    val id: Int,
    val title: String,
    val duration: String = ""
)

object SingersData {
    val singers = listOf(
        Singer(
            id = 1,
            name = "أيوب طارش",
            description = "فنان الشعب اليمني",
            songsCount = 12,
            emoji = "🎤",
            songs = listOf(
                Song(1, "أيوب طارش - وردة", "4:32"),
                Song(2, "أيوب طارش - يا زارعين العنب", "5:10"),
                Song(3, "أيوب طارش - موطني", "3:45"),
                Song(4, "أيوب طارش - عدن عدن", "4:20"),
                Song(5, "أيوب طارش - يا بنت بلادي", "4:55"),
                Song(6, "أيوب طارش - أنا يمني", "3:30"),
                Song(7, "أيوب طارش - صنعاء يا قلعة", "5:40"),
                Song(8, "أيوب طارش - يا مطر يا مطر", "4:15"),
                Song(9, "أيوب طارش - تعز الخضراء", "3:50"),
                Song(10, "أيوب طارش - يا قمر", "4:00"),
                Song(11, "أيوب طارش - حب الوطن", "5:25"),
                Song(12, "أيوب طارش - يا صنعاء", "4:45"),
            )
        ),
        Singer(
            id = 2,
            name = "أبو بكر سالم",
            description = "سلطان الطرب الحضرمي",
            songsCount = 10,
            emoji = "🎵",
            songs = listOf(
                Song(1, "أبو بكر سالم - عادك إلا صغير", "5:20"),
                Song(2, "أبو بكر سالم - يا سعد", "4:45"),
                Song(3, "أبو بكر سالم - بنت الخيال", "6:10"),
                Song(4, "أبو بكر سالم - شفتك يا مقدار", "4:30"),
                Song(5, "أبو بكر سالم - يا ظالمني", "5:15"),
                Song(6, "أبو بكر سالم - من علمك", "4:55"),
                Song(7, "أبو بكر سالم - يا شوق", "3:40"),
                Song(8, "أبو بكر سالم - على مودك", "4:20"),
                Song(9, "أبو بكر سالم - يا حبيبي", "5:00"),
                Song(10, "أبو بكر سالم - ليلة خميس", "4:35"),
            )
        ),
        Singer(
            id = 3,
            name = "فيصل علوي",
            description = "صوت عدن الذهبي",
            songsCount = 8,
            emoji = "🎶",
            songs = listOf(
                Song(1, "فيصل علوي - يا حبيبتي", "4:10"),
                Song(2, "فيصل علوي - سألتك بالله", "5:30"),
                Song(3, "فيصل علوي - يا بنت بلادي", "4:45"),
                Song(4, "فيصل علوي - قلبي معاك", "3:55"),
                Song(5, "فيصل علوي - يا عمري", "4:20"),
                Song(6, "فيصل علوي - أنا وحبيبي", "5:00"),
                Song(7, "فيصل علوي - يا سمرة", "4:35"),
                Song(8, "فيصل علوي - لا تلوموني", "4:50"),
            )
        ),
        Singer(
            id = 4,
            name = "محمد حمود الحارثي",
            description = "أسطورة الأغنية الصنعانية",
            songsCount = 8,
            emoji = "🎹",
            songs = listOf(
                Song(1, "محمد حمود الحارثي - يا بهية", "5:15"),
                Song(2, "محمد حمود الحارثي - صنعانية", "4:40"),
                Song(3, "محمد حمود الحارثي - يا حلوة", "3:55"),
                Song(4, "محمد حمود الحارثي - غنوة صنعاء", "5:30"),
                Song(5, "محمد حمود الحارثي - يا قمري", "4:10"),
                Song(6, "محمد حمود الحارثي - بنت اليمن", "4:45"),
                Song(7, "محمد حمود الحارثي - يا ليالي", "5:00"),
                Song(8, "محمد حمود الحارثي - طرب صنعاني", "4:25"),
            )
        ),
        Singer(
            id = 5,
            name = "علي بن علي الآنسي",
            description = "ملك الأغنية اليمنية الأصيلة",
            songsCount = 7,
            emoji = "🎻",
            songs = listOf(
                Song(1, "علي بن علي الآنسي - يا حبيبي تعال", "4:30"),
                Song(2, "علي بن علي الآنسي - من يمن", "5:10"),
                Song(3, "علي بن علي الآنسي - أغنية يمنية", "4:15"),
                Song(4, "علي بن علي الآنسي - يا قلبي", "3:45"),
                Song(5, "علي بن علي الآنسي - سهرة يمنية", "5:40"),
                Song(6, "علي بن علي الآنسي - طرب أصيل", "4:55"),
                Song(7, "علي بن علي الآنسي - يا ليل", "4:20"),
            )
        ),
    )

    fun getSingerById(id: Int): Singer? = singers.find { it.id == id }
}
