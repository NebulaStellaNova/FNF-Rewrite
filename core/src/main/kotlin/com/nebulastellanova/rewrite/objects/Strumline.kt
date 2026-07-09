package com.nebulastellanova.rewrite.objects

import com.nebulastellanova.rewrite.internal.data.SongNoteData
import com.nebulastellanova.rewrite.states.PlayState
import org.flixelgdx.group.FlixelSpriteGroup
import kotlin.math.abs
import kotlin.math.floor

class Strumline : FlixelSpriteGroup {
    val swagWidth = 160 * 0.7
    var notes: Array<NoteSprite> = arrayOf()
    var strums: Array<StrumSprite> = arrayOf()

    var cpu: Boolean = true

    constructor(notes: List<SongNoteData>, cpu: Boolean = true) : super() {
        this.cpu = cpu
        for (i in 0 until 4) {
            var strum = StrumSprite(i)
            strum.x = (swagWidth * strum.direction).toFloat()

            strum.animation?.onAnimationFinished?.add {
                if (it.animationName.trim() == "confirm")
                    strum.animation?.playAnimation(if (!cpu) "press" else "idle")
            }
            strums += strum
            add(strum)
        }

        for (i in notes) {
            val note = NoteSprite(floor(i.d % 4.0).toInt(), i.t, i.l, i.k)
            note.x = (swagWidth * note.id).toFloat()
            this.notes += (note)
        }
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)
        notes.forEach { i ->
            var daY = (((PlayState.conductor.songPosition - i.time) * 0.45 * abs(PlayState.scrollSpeed)) + this.y).toFloat()
            if (daY > this.y && cpu && PlayState.conductor.songPosition > 0) {
                if (notes.contains(i)) {
                    remove(i)
                    notes = notes.filter { it != i }.toTypedArray()
                }
                strums[i.id].animation?.playAnimation("confirm", false, true)
            } else {
                if (daY > -100 && !members.contains(i)) add(i)
            }
            i.y = daY
        }
    }
}
