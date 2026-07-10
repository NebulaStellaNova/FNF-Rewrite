package com.nebulastellanova.rewrite.objects

import com.nebulastellanova.rewrite.internal.data.SongNoteData
import com.nebulastellanova.rewrite.states.PlayState
import org.flixelgdx.group.FlixelSpriteGroup
import kotlin.math.abs
import kotlin.math.floor

class Strumline : FlixelSpriteGroup {
    val swagWidth = 160 * 0.7

    var notes: MutableList<NoteSprite> = ArrayList()
    var strums: Array<StrumSprite> = arrayOf()

    var cpu: Boolean = true

    private var visualSongPosition: Double = 0.0
    private var lastRawSongPosition: Double = -1.0

    constructor(notes: List<SongNoteData>, cpu: Boolean = true) : super() {
        this.cpu = cpu

        strums = Array(4) { i ->
            val strum = StrumSprite(i)
            strum.x = (swagWidth * strum.direction).toFloat()
            strum.animation?.onAnimationFinished?.add {
                if (it.animationName.trim() == "confirm")
                    strum.animation?.playAnimation(if (!cpu) "press" else "idle")
            }
            add(strum)
            strum
        }

        this.notes = ArrayList(notes.size)
        for (n in notes) {
            val note = NoteSprite(floor(n.d % 4.0).toInt(), n.t, n.l, n.k)
            note.x = (swagWidth * note.id).toFloat()
            this.notes.add(note)
        }
        this.notes.sortBy { it.time }
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)

        val rawSongPosition = PlayState.conductor.songPosition

        if (rawSongPosition != lastRawSongPosition) {
            val delta = abs(rawSongPosition - visualSongPosition)
            visualSongPosition = if (delta > 50.0) rawSongPosition
            else (visualSongPosition + rawSongPosition) / 2.0
            lastRawSongPosition = rawSongPosition
        } else {
            visualSongPosition += elapsed * 1000.0
        }

        val speedFactor = 0.45 * abs(PlayState.scrollSpeed)
        val thisY = this.y
        val cpuActive = cpu && rawSongPosition > 0

        val it = notes.iterator()
        while (it.hasNext()) {
            val note = it.next()
            val daY = (((visualSongPosition - note.time) * speedFactor) + thisY).toFloat()

            if (daY > thisY && cpuActive) {
                remove(note)
                it.remove()
                strums[note.id].animation?.playAnimation("confirm", false, true)
            } else if (daY > -100 && !members.contains(note)) {
                add(note)
            }

            note.y = daY
        }
    }
}

