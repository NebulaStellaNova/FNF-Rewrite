package com.nebulastellanova.rewrite

import com.nebulastellanova.rewrite.internal.modding.ModdingAPI
import com.nebulastellanova.rewrite.states.PlayState
import com.nebulastellanova.rewrite.states.TitleState
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelGame
import org.flixelgdx.input.keyboard.FlixelKey

/**
 * Your game entry class.
 *
 * FlixelGame owns the window settings and picks the first FlixelState.
 *
 * If you are new here, start in PlayState.kt. That file is where you spawn sprites,
 * load sounds, and write your first update loop.
 */
class RewriteGame : FlixelGame("Friday Night Funkin': Rewrite", 1280, 720, TitleState()) {
    override fun create() {
        Flixel.sound.masterVolume = 0.5f
        super.create()

        ModdingAPI.init()
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)

        if (Flixel.keys.justPressed(FlixelKey.EQUALS)) {
            Flixel.sound.masterVolume += 0.1f
            if (Flixel.sound.masterVolume > 0.5f)
                Flixel.sound.masterVolume = 0.5f
        } else if (Flixel.keys.justPressed(FlixelKey.MINUS)) {
            Flixel.sound.masterVolume -= 0.1f
            if (Flixel.sound.masterVolume < 0.0f)
                Flixel.sound.masterVolume = 0.0f
        } else if (Flixel.keys.justPressed(FlixelKey.NUM_0)) {
            Flixel.sound.masterVolume = 0.0f
        }
    }
}
