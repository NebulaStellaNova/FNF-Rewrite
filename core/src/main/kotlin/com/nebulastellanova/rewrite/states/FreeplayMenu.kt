package com.nebulastellanova.rewrite.states

import com.badlogic.gdx.Gdx
import com.nebulastellanova.rewrite.utils.ParseUtil
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelState

class FreeplayMenu : FlixelState() {

    var curSelected: Int = 0;

    override fun create() {
        super.create()

        /* var order = ParseUtil.getStringFromPath("songs/order.txt").split("\n").filter { it.isNotEmpty() }.filter { !it.trim().startsWith("#") }
        Flixel.info(order);

        for (i in order) {
            val jsonDataBytes = ParseUtil.getFileFromZip("songs/$i.fnfc", "$i-metadata.json")

            if (jsonDataBytes != null) {
                val jsonString = String(jsonDataBytes, Charsets.UTF_8)
                Gdx.app.log("ZIP", "Successfully read file: $jsonString")
            } else {
                Gdx.app.error("ZIP", "File not found!")
            }
        } */

        Flixel.switchState(PlayState())
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)
    }
}
