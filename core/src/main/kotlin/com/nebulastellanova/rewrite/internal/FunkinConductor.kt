package com.nebulastellanova.rewrite.internal

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Json
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelBasic
import org.flixelgdx.audio.FlixelSound
import kotlin.math.abs
import kotlin.math.floor

class MusicMeta {
    var bpm: Double = 0.0
}


class FunkinConductor: FlixelBasic() {
    var additionalTracks: Array<FlixelSound> = emptyArray()

    var bpm:Double = 0.0
    var numerator:Int = 4
    var denominator:Int = 4

    val beatScale: Double
        get() { return 4.0 / denominator }
    val secondsPerBeat: Double
        get() { return (60.0 / bpm) * beatScale }
    val stepsPerBeat: Int
        get() { return if (denominator == 8) 2 else 4 }
    val secondsPerStep: Double
        get() { return secondsPerBeat / stepsPerBeat }

    val songPosition: Double
        get() { return track?.time?.toDouble() ?: 0.0 }

    var curBeat: Int = 0
    var curBeatDouble: Double = 0.0
    var curStep: Int = 0
    var curStepDouble: Double = 0.0
    var curMeasure: Int = 0
    var curMeasureDouble: Double = 0.0

    var track: FlixelSound? = null

    fun loadTrack(path: String) {
        track = Flixel.sound.play("$path/audio.mp3")
        track?.stop()


        var json = Json()
        val fileHandle = Gdx.files.internal("$path/meta.json")

        var meta: MusicMeta = json.fromJson(MusicMeta::class.java, fileHandle)
        this.bpm = meta.bpm
    }

    var prevBeat = 0
    override fun update(elapsed: Float) {
        super.update(elapsed)
        for (i in additionalTracks) {
//            i.time = track!!.time
        }

        val time = songPosition / 1000

        val beatScale = 4.0 / denominator
        val secondsPerBeat = (60.0 / bpm) * beatScale
        val stepsPerBeat = if (denominator == 8) 2 else 4
        val secondsPerStep = secondsPerBeat / stepsPerBeat

        val exactTotalBeats: Double = time / secondsPerBeat

        curBeat = exactTotalBeats.toInt()
        curBeatDouble = exactTotalBeats
        curStep = (time / secondsPerStep).toInt()
        curStepDouble = (time / secondsPerStep)
        curMeasure = curBeat / numerator
        curMeasureDouble = curBeatDouble / numerator

        if (prevBeat != curBeat) {
            println(curBeat)
            prevBeat = curBeat
        }
    }

    fun addAdditionalTrack(track: FlixelSound) {
        additionalTracks += track
    }

    fun play() {
        track?.play()
        for (i in additionalTracks) {
            i.time = track?.time!!
            i.play()
        }
    }

    fun pause() {
        track?.pause()
        for (i in additionalTracks) i.pause()
    }

    fun resume() {
        play()
    }

    fun stop() {
        track?.stop()
        for (i in additionalTracks) i.stop()
    }
}
