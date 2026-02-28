package com.yemeni.songs.data

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
    val duration: String = "",
    val rawResId: Int = 0
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
                Song(1, "وردة", "4:32"),
                Song(2, "يا زارعين العنب", "5:10"),
                Song(3, "موطني", "3:45"),
                Song(4, "عدن عدن", "4:20"),
                Song(5, "يا بنت بلادي", "4:55"),
                Song(6, "أنا يمني", "3:30"),
                Song(7, "صنعاء يا قلعة", "5:40"),
                Song(8, "يا مطر يا مطر", "4:15"),
                Song(9, "تعز الخضراء", "3:50"),
                Song(10, "يا قمر", "4:00"),
                Song(11, "حب الوطن", "5:25"),
                Song(12, "يا صنعاء", "4:45"),
            )
        ),
        Singer(
            id = 2,
            name = "أبو بكر سالم",
            description = "سلطان الطرب الحضرمي",
            songsCount = 10,
            emoji = "🎵",
            songs = listOf(
                Song(1, "عادك إلا صغير", "5:20"),
                Song(2, "يا سعد", "4:45"),
                Song(3, "بنت الخيال", "6:10"),
                Song(4, "شفتك يا مقدار", "4:30"),
                Song(5, "يا ظالمني", "5:15"),
                Song(6, "من علمك", "4:55"),
                Song(7, "يا شوق", "3:40"),
                Song(8, "على مودك", "4:20"),
                Song(9, "يا حبيبي", "5:00"),
                Song(10, "ليلة خميس", "4:35"),
            )
        ),
        Singer(
            id = 3,
            name = "فيصل علوي",
            description = "صوت عدن الذهبي",
            songsCount = 8,
            emoji = "🎶",
            songs = listOf(
                Song(1, "يا حبيبتي", "4:10"),
                Song(2, "سألتك بالله", "5:30"),
                Song(3, "يا بنت بلادي", "4:45"),
                Song(4, "قلبي معاك", "3:55"),
                Song(5, "يا عمري", "4:20"),
                Song(6, "أنا وحبيبي", "5:00"),
                Song(7, "يا سمرة", "4:35"),
                Song(8, "لا تلوموني", "4:50"),
            )
        ),
        Singer(
            id = 4,
            name = "محمد حمود الحارثي",
            description = "أسطورة الأغنية الصنعانية",
            songsCount = 8,
            emoji = "🎹",
            songs = listOf(
                Song(1, "يا بهية", "5:15"),
                Song(2, "صنعانية", "4:40"),
                Song(3, "يا حلوة", "3:55"),
                Song(4, "غنوة صنعاء", "5:30"),
                Song(5, "يا قمري", "4:10"),
                Song(6, "بنت اليمن", "4:45"),
                Song(7, "يا ليالي", "5:00"),
                Song(8, "طرب صنعاني", "4:25"),
            )
        ),
        Singer(
            id = 5,
            name = "علي بن علي الآنسي",
            description = "ملك الأغنية اليمنية الأصيلة",
            songsCount = 7,
            emoji = "🎻",
            songs = listOf(
                Song(1, "يا حبيبي تعال", "4:30"),
                Song(2, "من يمن", "5:10"),
                Song(3, "أغنية يمنية", "4:15"),
                Song(4, "يا قلبي", "3:45"),
                Song(5, "سهرة يمنية", "5:40"),
                Song(6, "طرب أصيل", "4:55"),
                Song(7, "يا ليل", "4:20"),
            )
        ),
    )

    fun getSingerById(id: Int): Singer? = singers.find { it.id == id }
}
