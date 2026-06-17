package com.nebulastellanova.rewrite.internal

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelBasic
import org.flixelgdx.audio.FlixelSound

class FunkinConductor : FlixelBasic() {

//    companion object {
//        private val jsonReader = JsonReader()
//    }

    var bpm: Double = 0.0
    var curBeatDouble: Double = 0.0
    var curStepDouble: Double = 0.0
    var curMeasureDouble: Double = 0.0

    var numerator: Int = 4
    var denominator: Int = 4
    var curBeat: Int = 0
    var curStep: Int = 0
    var curMeasure: Int = 0
    private var prevBeat: Int = 0
    var track: FlixelSound? = null

    val beatScale: Double
        get() = 4.0 / denominator
    val secondsPerBeat: Double
        get() = (60.0 / bpm) * beatScale
    val stepsPerBeat: Int
        get() = if (denominator == 8) 2 else 4
    val secondsPerStep: Double
        get() = secondsPerBeat / stepsPerBeat
    val songPosition: Double
        get() = track?.time?.toDouble() ?: 0.0

    fun loadTrack(path: String) {
        track = Flixel.sound.play("$path/audio.mp3")
        track?.stop()

//        val meta = jsonReader.parse(Gdx.files.internal("$path/meta.json"))
//        bpm = meta.getDouble("bpm")
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)
        val time = songPosition / 1000.0
        val exactTotalBeats = time / secondsPerBeat
        val exactTotalSteps = time / secondsPerStep

        curBeat = exactTotalBeats.toInt()
        curBeatDouble = exactTotalBeats
        curStep = exactTotalSteps.toInt()
        curStepDouble = exactTotalSteps
        curMeasure = curBeat / numerator
        curMeasureDouble = curBeatDouble / numerator

        if (prevBeat != curBeat) {
            prevBeat = curBeat
        }
    }
}
