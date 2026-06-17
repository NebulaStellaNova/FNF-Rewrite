package com.nebulastellanova.rewrite.states

import com.nebulastellanova.rewrite.util.MathUtil.wrap
import com.nebulastellanova.rewrite.util.ParseUtil
import com.nebulastellanova.rewrite.util.Paths
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelSprite
import org.flixelgdx.FlixelState
import org.flixelgdx.animation.FlixelAnimationController
import org.flixelgdx.input.keyboard.FlixelKey
import org.flixelgdx.tween.FlixelTween
import org.flixelgdx.tween.settings.FlixelTweenSettings
import org.flixelgdx.util.FlixelAxes
import org.flixelgdx.util.timer.FlixelTimer
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList

class MainMenu : FlixelState() {
    lateinit var background: FlixelSprite

    var items = arrayListOf<FlixelSprite>()
    var ids = arrayListOf<String>()
    var targets = arrayListOf<String?>()
    var curSelected = 0
    var canSelect: Boolean = true

    override fun create() {
        super.create()

        items.clear()

        var menuData: Document? = ParseUtil.loadXmlFromPath("assets/data/config/mainmenu.xml")

        background = FlixelSprite(-150f, 100f)
        background.loadGraphic(Paths.image("menus/main/menuBG"))
        background.updateHitbox()
        background.screenCenter()
        background.setColor(253f / 255f, 232f / 255f, 75f / 255f, 1.0f)
        add(background)

        if (menuData != null) {
            var element = menuData.documentElement;
            element.normalize()
            var buttonList: NodeList = element.getElementsByTagName("button");

            for (i in 0 until buttonList.length) {
                val node = buttonList.item(i)
                val element = node as Element
                var target: String? = if (element.getAttribute("target").toString().trim() != "") element.getAttribute("target") else null
                var imgPath: String = element.getAttribute("asset");
                var idle = element.getElementsByTagName("idle").item(0) as Element;
                var iOffsetX: Float =
                    if (idle.getAttribute("x").toString().trim() != "") idle.getAttribute("x").toFloat() else 0f;
                var iOffsetY: Float =
                    if (idle.getAttribute("y").toString().trim() != "") idle.getAttribute("y").toFloat() else 0f;
                var selected = element.getElementsByTagName("selected").item(0) as Element;
                var sOffsetX: Float = if (selected.getAttribute("x").toString().trim() != "") selected.getAttribute("x")
                    .toFloat() else 0f;
                var sOffsetY: Float = if (selected.getAttribute("y").toString().trim() != "") selected.getAttribute("y")
                    .toFloat() else 0f;

                var button: FlixelSprite = FlixelSprite(0f, -150f * i)
                button.y += 720 - 200
                button.animation = FlixelAnimationController(button)
                button.animation?.loadSparrowFrames(Paths.image(imgPath), Paths.sparrow(imgPath))
                button.animation?.addAnimationByPrefix("idle", idle.getAttribute("prefix"), 24, true)
                button.animation?.addAnimationByPrefix("selected", selected.getAttribute("prefix"), 24, true)
                button.animation?.playAnimation("idle")
                if (iOffsetX != 0f || iOffsetY != 0f)
                    button.animation?.addOffset("idle", iOffsetX, iOffsetY)
                if (sOffsetX != 0f || sOffsetY != 0f)
                    button.animation?.addOffset("selected", sOffsetX, sOffsetY)
                button.setOriginCenter()
                button.updateHitbox()
                button.screenCenter(FlixelAxes.X)
                items.add(button)
                ids.add(element.getAttribute("id"))
                targets.add(target)
                add(button)
            }
        }
    }

    override fun update(elapsed: Float) {
        super.update(elapsed)

        if (Flixel.keys.justPressed(FlixelKey.F4)) {
            Flixel.switchState(MainMenu())
        }

        var i = 0
        for (item in items) {
            var animName: String = if (i != curSelected) "idle" else "selected"
            if (item.animation?.currentAnim != animName) {
                item.animation?.playAnimation(animName)
            }
            if (i == curSelected && Flixel.keys.justPressed(FlixelKey.ENTER) && canSelect) {
                Flixel.sound.play("sounds/menu/confirm.mp3")
                FlixelTween.flicker(item, 0.1f, 0.5f, false, FlixelTweenSettings(), null)
                FlixelTimer.wait(1f, fun (timer) {
                    when (targets[curSelected]) {
                        else -> {
                            Flixel.warn("Target with id \"${targets[curSelected]}\" does not exist! Ignoring input.")
                            canSelect = true
                            item.visible = true
                        }
                    }
                })
                canSelect = false
            }
            i++
        }

        if (Flixel.keys.justPressed(FlixelKey.DOWN) && canSelect) {
            curSelected++
            curSelected = curSelected.wrap(0, items.size)
            Flixel.sound.play("sounds/menu/scroll.mp3")
        }
        if (Flixel.keys.justPressed(FlixelKey.UP) && canSelect) {
            curSelected--
            curSelected = curSelected.wrap(0, items.size)
            Flixel.sound.play("sounds/menu/scroll.mp3")
        }


    }


}

