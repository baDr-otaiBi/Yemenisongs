package com.yemeni.songs.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.yemeni.songs.data.Song
import com.yemeni.songs.data.Singer
import com.yemeni.songs.data.SongStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MusicPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    val songStorage = SongStorage(context)

    var isPlaying by mutableStateOf(false)
        private set

    var currentSong by mutableStateOf<Song?>(null)
        private set

    var currentSinger by mutableStateOf<Singer?>(null)
        private set

    var currentSongIndex by mutableIntStateOf(0)
        private set

    var progress by mutableFloatStateOf(0f)
        private set

    var currentPosition by mutableLongStateOf(0L)
        private set

    var totalDuration by mutableLongStateOf(0L)
        private set

    var currentPlaylist by mutableStateOf<List<Song>>(emptyList())
        private set

    val hasSong: Boolean get() = currentSong != null

    fun playSong(song: Song, singer: Singer, playlist: List<Song>, index: Int) {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            progressJob?.cancel()

            currentSong = song
            currentSinger = singer
            currentPlaylist = playlist
            currentSongIndex = index
            progress = 0f
            currentPosition = 0L

            // Try local file first
            val localPath = songStorage.getSongPath(singer.id, song.id)
            if (localPath != null) {
                playFromFile(localPath)
                return
            }

            // Try raw resource
            val rawResId = song.rawResId
            if (rawResId != 0) {
                mediaPlayer = MediaPlayer.create(context, rawResId)
                if (mediaPlayer != null) {
                    mediaPlayer!!.setOnCompletionListener { playNext() }
                    totalDuration = mediaPlayer!!.duration.toLong()
                    mediaPlayer!!.start()
                    isPlaying = true
                    startProgressTracking()
                    return
                }
            }

            // Simulate playback if no audio source
            isPlaying = true
            totalDuration = parseDuration(song.duration)
            startProgressTracking()
        } catch (e: Exception) {
            isPlaying = true
            totalDuration = parseDuration(song.duration)
            startProgressTracking()
        }
    }

    private fun playFromFile(filePath: String) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                setOnCompletionListener { playNext() }
                totalDuration = duration.toLong()
                start()
            }
            isPlaying = true
            startProgressTracking()
        } catch (e: Exception) {
            // Fallback to simulation
            isPlaying = true
            totalDuration = parseDuration(currentSong?.duration ?: "4:00")
            startProgressTracking()
        }
    }

    fun importSong(singerId: Int, songId: Int, uri: Uri): Boolean {
        val path = songStorage.saveSong(singerId, songId, uri)
        return path != null
    }

    private fun parseDuration(duration: String): Long {
        val parts = duration.split(":")
        if (parts.size == 2) {
            val minutes = parts[0].toLongOrNull() ?: 0
            val seconds = parts[1].toLongOrNull() ?: 0
            return (minutes * 60 + seconds) * 1000
        }
        return 240_000L
    }

    fun togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer?.pause()
                progressJob?.cancel()
            } else {
                mediaPlayer?.start()
                startProgressTracking()
            }
        }
        isPlaying = !isPlaying
    }

    fun playNext() {
        if (currentPlaylist.isEmpty()) return
        val nextIndex = (currentSongIndex + 1) % currentPlaylist.size
        val nextSong = currentPlaylist[nextIndex]
        currentSinger?.let { playSong(nextSong, it, currentPlaylist, nextIndex) }
    }

    fun playPrevious() {
        if (currentPlaylist.isEmpty()) return
        val prevIndex = if (currentSongIndex > 0) currentSongIndex - 1 else currentPlaylist.size - 1
        val prevSong = currentPlaylist[prevIndex]
        currentSinger?.let { playSong(prevSong, it, currentPlaylist, prevIndex) }
    }

    fun seekTo(position: Float) {
        progress = position
        val seekPos = (position * totalDuration).toLong()
        currentPosition = seekPos
        mediaPlayer?.seekTo(seekPos.toInt())
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isPlaying) {
                if (mediaPlayer != null) {
                    try {
                        currentPosition = mediaPlayer!!.currentPosition.toLong()
                        totalDuration = mediaPlayer!!.duration.toLong()
                        progress = if (totalDuration > 0) currentPosition.toFloat() / totalDuration else 0f
                    } catch (_: Exception) {}
                } else {
                    if (totalDuration > 0 && currentPosition < totalDuration) {
                        currentPosition += 500
                        progress = currentPosition.toFloat() / totalDuration
                    } else if (currentPosition >= totalDuration) {
                        playNext()
                    }
                }
                delay(500)
            }
        }
    }

    fun release() {
        progressJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }
}

fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
