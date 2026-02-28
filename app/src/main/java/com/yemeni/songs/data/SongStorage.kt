package com.yemeni.songs.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class SongStorage(private val context: Context) {

    private val songsDir: File
        get() {
            val dir = File(context.filesDir, "songs")
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

    /**
     * Get the directory for a specific singer's songs
     */
    private fun singerDir(singerId: Int): File {
        val dir = File(songsDir, "singer_$singerId")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /**
     * Save a song file from a content URI to internal storage.
     * Returns the saved file path.
     */
    fun saveSong(singerId: Int, songId: Int, uri: Uri): String? {
        return try {
            val file = File(singerDir(singerId), "song_${songId}.mp3")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Check if a song file exists in local storage
     */
    fun hasSong(singerId: Int, songId: Int): Boolean {
        val file = File(singerDir(singerId), "song_${songId}.mp3")
        return file.exists() && file.length() > 0
    }

    /**
     * Get the file path for a saved song
     */
    fun getSongPath(singerId: Int, songId: Int): String? {
        val file = File(singerDir(singerId), "song_${songId}.mp3")
        return if (file.exists()) file.absolutePath else null
    }

    /**
     * Delete a saved song
     */
    fun deleteSong(singerId: Int, songId: Int): Boolean {
        val file = File(singerDir(singerId), "song_${songId}.mp3")
        return if (file.exists()) file.delete() else false
    }

    /**
     * Get count of saved songs for a singer
     */
    fun getSavedSongCount(singerId: Int): Int {
        val dir = singerDir(singerId)
        return dir.listFiles()?.count { it.extension == "mp3" } ?: 0
    }

    /**
     * Get all saved song IDs for a singer
     */
    fun getSavedSongIds(singerId: Int): Set<Int> {
        val dir = singerDir(singerId)
        return dir.listFiles()
            ?.filter { it.extension == "mp3" }
            ?.mapNotNull { file ->
                file.nameWithoutExtension
                    .removePrefix("song_")
                    .toIntOrNull()
            }
            ?.toSet() ?: emptySet()
    }
}
