package com.nebulastellanova.rewrite

import com.badlogic.gdx.Gdx
import com.nebulastellanova.rewrite.internal.modding.ModdingAPI
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
class FunkinGame : FlixelGame("Friday Night Funkin': Rewrite", 1280, 720, TitleState(), 60, true) {

    override fun create() {
        super.create()

        // libGDX configs.
        Gdx.app.applicationLogger = Flixel.log

        // FlixelGDX configs.
        Flixel.setAntialiasing(true)

        // Funkin' configs.
        ModdingAPI.init()
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)

        if (Flixel.keys.justPressed(FlixelKey.F11)) {
            toggleFullscreen()
        }
    }
}
