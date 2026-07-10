package com.nebulastellanova.rewrite.objects

import com.nebulastellanova.rewrite.utils.Paths
import org.flixelgdx.FlixelSprite
import org.flixelgdx.animation.FlixelAnimationController

class NoteSprite : FlixelSprite {

    var id: Int = 0
    var time: Float = 0.0f
    var tailLength: Float = 0.0f
    var noteType: String = ""

    constructor(id: Int, time: Float, tailLength: Float, noteType: String) : super() {
        this.id = id
        this.time = time
        this.tailLength = tailLength
        this.noteType = noteType


        this.animation = FlixelAnimationController(this)
        this.animation?.addSparrowAtlas(Paths.sparrow("game/notes/default/notes"))
        this.animation?.addAnimationByPrefix("idle", "note${arrayOf("Left", "Down", "Up", "Right")[id]}", 24, true)
        this.animation?.playAnimation("idle")
        this.setScale(0.7f)
        this.updateHitbox()
    }
}
