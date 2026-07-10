package com.nebulastellanova.rewrite.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.nebulastellanova.rewrite.internal.Constants
import com.nebulastellanova.rewrite.internal.Controls
import com.nebulastellanova.rewrite.internal.FunkinConductor
import com.nebulastellanova.rewrite.utils.Paths
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelSprite
import org.flixelgdx.FlixelState
import org.flixelgdx.animation.FlixelAnimateSprite
import org.flixelgdx.animation.FlixelAnimationController
import org.flixelgdx.input.keyboard.FlixelKey
import org.flixelgdx.text.FlixelFontRegistry
import org.flixelgdx.tween.FlixelTween
import org.flixelgdx.tween.settings.FlixelTweenSettings
import org.flixelgdx.tween.settings.FlixelTweenType
import org.flixelgdx.util.FlixelAxes
import org.flixelgdx.util.FlixelColor
import org.flixelgdx.util.timer.FlixelTimer
import javax.naming.ldap.Control

class TitleState : FlixelState() {
    lateinit var logo: FlixelSprite
    lateinit var gf: FlixelSprite
    lateinit var text: FlixelAnimateSprite

    companion object {
        var menuTheme: FunkinConductor? = null
    }

    var canSelect: Boolean = true

    override fun create() {
        super.create()

        FlixelFontRegistry.register("VCR OSD Mono", Gdx.files.internal("assets/fonts/vcr.ttf"))

        if (menuTheme == null) {
            menuTheme = FunkinConductor()
            menuTheme?.loadTrack("music/freakyMenu")
            menuTheme?.track?.isPersist = true
            menuTheme?.track?.play()
            menuTheme?.track?.isLooped = true
            add(menuTheme!!)
        }

        Flixel.setAntialiasing(true)

        logo = FlixelSprite(-150f, 100f)
        logo.animation = FlixelAnimationController(logo)
        logo.animation?.addSparrowAtlas(Paths.sparrow("menus/title/logoBumpin"))
        logo.animation?.addAnimationByPrefix("idle", "logo bumpin", 24, true)
        logo.animation?.playAnimation("idle")
        add(logo)

        gf = FlixelSprite(512f, 40f)
        gf.animation = FlixelAnimationController(gf)
        gf.animation?.addSparrowAtlas(Paths.sparrow("menus/title/gfDanceTitle"))
        gf.animation?.addAnimationByPrefix("idle", "gfDance", 24, true)
        gf.animation?.playAnimation("idle")
        add(gf)

        text = FlixelAnimateSprite(0f, 40f)
        text.addSpritemapAndAnimation(
            "images/menus/title/title-screen-text/spritemap1.png",
            "images/menus/title/title-screen-text/spritemap1.json",
            "images/menus/title/title-screen-text/Animation.json"
        )
        text.setAntialiasing(true)
        text.animation?.playAnimation("Idle", true)
        text.updateHitbox()
        text.screenCenter(FlixelAxes.X)
        text.changeX(225f)
        add(text)

        FlixelTween.color(
            text, FlixelColor(0f, 255f, 255f, 255f), FlixelColor(56f, 65f, 187f, 100f),
            FlixelTweenSettings().setType(FlixelTweenType.PINGPONG)
        )

        Flixel.info(text)

        Flixel.cameras.first().flash(FlixelColor.WHITE, 1f)

        for (i in members!!) {
            if (i is FlixelSprite)
                i.isAntialiasing = true
        }
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)
        if (Controls.ACCEPT) {
            var timer: FlixelTimer = FlixelTimer()
            if (canSelect) {
                FlixelTween.cancelTweensOf(text)
                text.setColor(FlixelColor.WHITE)
                text.animation?.playAnimation("Confirm")

                Flixel.cameras.first().flash(FlixelColor.WHITE, 1f)
                Flixel.sound.play("sounds/menu/confirm.mp3")
                canSelect = false
                timer = FlixelTimer.wait(2f, fun(timer: FlixelTimer) {
                    Flixel.switchState(MainMenu())
                })
            } else {
                timer.cancel()
                Flixel.switchState(MainMenu())
            }
        }
    }
}
