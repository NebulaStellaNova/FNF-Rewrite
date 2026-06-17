package com.nebulastellanova.rewrite.states

import com.badlogic.gdx.utils.Array as GdxArray
import com.badlogic.gdx.utils.XmlReader
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

class MainMenu : FlixelState() {
    lateinit var background: FlixelSprite

    val items: GdxArray<FlixelSprite> = GdxArray()
    val ids: GdxArray<String> = GdxArray()
    val targets: GdxArray<String?> = GdxArray()
    var curSelected = 0
    var canSelect: Boolean = true

    override fun create() {
        super.create()

        items.clear()
        ids.clear()
        targets.clear()

        val menuData: XmlReader.Element? = ParseUtil.loadXml("data/config/mainmenu.xml")

        background = FlixelSprite(-150f, 100f)
        background.loadGraphic(Paths.image("menus/main/menuBG"))
        background.updateHitbox()
        background.screenCenter()
        background.setColor(253f / 255f, 232f / 255f, 75f / 255f, 1.0f)
        add(background)

        if (menuData != null) {
            val buttons = menuData.getChildrenByName("button")
            for (i in 0 until buttons.size) {
                val element: XmlReader.Element = buttons[i]
                val target: String? = element.getAttribute("target", "").trim().takeIf { it.isNotEmpty() }
                val imgPath: String = element.getAttribute("asset")
                val idle: XmlReader.Element = element.getChildByName("idle")!!
                val iOffsetX: Float = idle.getFloatAttribute("x", 0f)
                val iOffsetY: Float = idle.getFloatAttribute("y", 0f)
                val selected: XmlReader.Element = element.getChildByName("selected")!!
                val sOffsetX: Float = selected.getFloatAttribute("x", 0f)
                val sOffsetY: Float = selected.getFloatAttribute("y", 0f)

                val button = FlixelSprite(0f, -150f * i)
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

        for (i in 0 until items.size) {
            val item = items[i]
            val animName: String = if (i != curSelected) "idle" else "selected"
            if (item.animation?.currentAnim != animName) {
                item.animation?.playAnimation(animName)
            }
            if (i == curSelected && Flixel.keys.justPressed(FlixelKey.ENTER) && canSelect) {
                Flixel.sound.play("sounds/menu/confirm.mp3")
                FlixelTween.flicker(item, 0.1f, 0.5f, false, FlixelTweenSettings(), null)
                FlixelTimer.wait(1f, fun(timer) {
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
        }

        if (Flixel.keys.justPressed(FlixelKey.DOWN) && canSelect) {
            scrollMenu(1)
        }
        if (Flixel.keys.justPressed(FlixelKey.UP) && canSelect) {
            scrollMenu(-1)
        }
    }

    private fun scrollMenu(dir: Int) {
        curSelected += dir
        curSelected = curSelected.wrap(0, items.size)
        Flixel.sound.play("sounds/menu/scroll.mp3")
    }
}
