package com.nebulastellanova.rewrite

import com.badlogic.gdx.Gdx
import com.nebulastellanova.rewrite.internal.modding.ModdingAPI
import com.nebulastellanova.rewrite.states.TitleState
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelGame

/**
 * Your game entry class.
 *
 * FlixelGame owns the window settings and picks the first FlixelState.
 *
 * If you are new here, start in PlayState.kt. That file is where you spawn sprites,
 * load sounds, and write your first update loop.
 */
class FunkinGame : FlixelGame("Friday Night Funkin': Rewrite", 1280, 720, TitleState()) {

    override fun create() {
        super.create()

        Gdx.app.applicationLogger = Flixel.log
        ModdingAPI.init()
    }
}
