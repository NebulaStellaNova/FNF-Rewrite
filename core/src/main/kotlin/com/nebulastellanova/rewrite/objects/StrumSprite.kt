package com.nebulastellanova.rewrite.objects

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.nebulastellanova.rewrite.utils.Paths
import org.flixelgdx.FlixelSprite
import org.flixelgdx.animation.FlixelAnimationController


class StrumSprite : FlixelSprite {

    var direction: Int = 0

    constructor(direction:Int) : super() {
        this.direction = direction

        this.animation = FlixelAnimationController(this)
        this.animation?.loadSparrowFrames(
            Paths.image("game/notes/default/strums"),
            Paths.sparrow("game/notes/default/strums")
        )
        this.animation?.addAnimationByPrefix("idle", "static${arrayOf("Left", "Down", "Up", "Right")[direction]}", 24, true)
        this.animation?.addAnimationByPrefix("confirm", "confirm${arrayOf("Left", "Down", "Up", "Right")[direction]}", 24, false)
        this.animation?.addAnimationByPrefix("press", "press${arrayOf("Left", "Down", "Up", "Right")[direction]}", 24, false)
        this.animation?.playAnimation("idle")
//        this.animation?.addOffset("confirm", 40f, 40f)
        this.setScale(0.7f)
        this.updateHitbox()
        this.offsetX = 40f;
        this.offsetY = 40f;
    }

}
