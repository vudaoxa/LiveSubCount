package net.lc.utils

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException

class AudioPlayer(var contex: Context) {
    var mp: MediaPlayer? = null
    //Play Audio
    fun playAudio(fileName: String, loop: Boolean) {
        try {
            mp = MediaPlayer()
            val descriptor = contex.assets
                    .openFd(fileName)
            mp?.apply {
                setDataSource(descriptor.fileDescriptor,
                        descriptor.startOffset, descriptor.length)
                descriptor.close()
                prepare()
                isLooping = loop
                start()
                setVolume(3f, 3f)
                setOnCompletionListener { release() }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Stop Audio
    fun stop() {
        mp?.apply {
            stop()
        }
    }

    companion object {
        private var audioPlayer: AudioPlayer? = null
        fun getInstance(context: Context): AudioPlayer {
            if (audioPlayer == null) {
                audioPlayer = AudioPlayer(context)
            }
            return audioPlayer!!
        }
    }
}

object AudioConstants {
    const val PATH_INCRE = "sounds/incre.mp3"
    const val PATH_DECRE = "sounds/noti.mp3"
    const val PATH_NOTI = "sounds/decre.mp3"
}