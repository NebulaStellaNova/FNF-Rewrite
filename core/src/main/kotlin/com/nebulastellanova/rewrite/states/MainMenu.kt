package com.nebulastellanova.rewrite.states

import com.nebulastellanova.rewrite.internal.Constants
import com.nebulastellanova.rewrite.internal.Controls
import com.nebulastellanova.rewrite.utils.MathUtil.wrap
import com.nebulastellanova.rewrite.utils.ParseUtil
import com.nebulastellanova.rewrite.utils.Paths
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelObject
import org.flixelgdx.FlixelSprite
import org.flixelgdx.FlixelState
import org.flixelgdx.animation.FlixelAnimationController
import org.flixelgdx.input.keyboard.FlixelKey
import org.flixelgdx.text.FlixelText
import org.flixelgdx.tween.FlixelTween
import org.flixelgdx.tween.settings.FlixelTweenSettings
import org.flixelgdx.util.FlixelAxes
import org.flixelgdx.util.FlixelColor
import org.flixelgdx.util.timer.FlixelTimer
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.awt.Desktop
import java.net.URI
import kotlin.math.roundToInt

class MainMenu : FlixelState() {
    lateinit var background: FlixelSprite

    var items = arrayListOf<FlixelSprite>()
    var ids = arrayListOf<String>()
    var targets = arrayListOf<String?>()
    var curSelected = 0
    var canSelect: Boolean = true
    var camFollow: FlixelObject = FlixelObject(0f, 0f, 1f, 1f);
    var versionTxt: FlixelText = FlixelText()

    override fun create() {
        super.create()

        items.clear()

        var menuData: Document? = ParseUtil.loadXmlFromPath("data/config/mainmenu.xml")

        background = FlixelSprite(-150f, 100f)
        background.loadGraphic(Paths.image("menus/main/menuBG"))

        background.setScrollFactor(0f, 0.17f)
        background.setGraphicSize((Flixel.getViewWidth() * 1.2).roundToInt(), (Flixel.getViewHeight() * 1.2).roundToInt());
        background.updateHitbox()
        background.screenCenter()
        add(background)

        if (menuData != null) {
            var element = menuData.documentElement;
            element.normalize()
            var buttonList: NodeList = element.getElementsByTagName("button");

            var spacing: Float = 160f;
            var top: Float = (Flixel.getViewHeight() - (spacing * (buttonList.length - 1))) / 2;

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

                var button: FlixelSprite = FlixelSprite(0f, 0f)
                button.y = top + spacing * ((buttonList.length-1) - i);
                button.setScrollFactor(0f, 0.4f);
                button.animation = FlixelAnimationController(button)
                button.animation?.addSparrowAtlas(Paths.sparrow(imgPath))
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

                button.animation?.centerOffsets()
            }
        }

        Flixel.cameras[0].follow(camFollow, null, 0.06f)
        camFollow.setPosition(items[curSelected].midpointX, items[curSelected].midpointY)

        versionTxt.x = 9f;
        versionTxt.y = 7f;
        versionTxt.isAntialiasing = true;
        versionTxt.setScrollFactor(0f, 0f);
        versionTxt.text = "Friday Night Funkin': Rewrite - v0.0.1";
        versionTxt.setFormat("VCR OSD Mono", 16, FlixelColor.WHITE, FlixelText.Alignment.RIGHT, FlixelText.BorderStyle.OUTLINE, FlixelColor.BLACK);
        add(versionTxt);

        for (i in members!!) {
            if (i is FlixelSprite)
                i.isAntialiasing = true
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

            if (i == curSelected && Controls.ACCEPT && canSelect) {
                Flixel.sound.play("sounds/menu/confirm.mp3")
                FlixelTween.flicker(item, 0.1f, 0.5f, false, FlixelTweenSettings(), null)
                FlixelTimer.wait(1f, fun (timer) {
                    if (targets[curSelected].toString().contains("://")) {
                        var url = targets[curSelected].toString()
                        val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
                        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(URI(url))
                            } catch (e: Exception) {
                                e.printStackTrace() // Handle IO or security exceptions
                            }
                        } else {
                            // Fallback for environments where AWT Desktop is unsupported
                            val os = System.getProperty("os.name").lowercase()
                            val runtime = Runtime.getRuntime()
                            when {
                                os.contains("win") -> runtime.exec(arrayOf("cmd", "/c", "start", url))
                                os.contains("mac") -> runtime.exec(arrayOf("open", url))
                                os.contains("nix") || os.contains("nux") -> runtime.exec(arrayOf("xdg-open", url))
                            }
                        }
                        canSelect = true
                        item.visible = true
                    } else {
                        when (targets[curSelected]) {
                            "FreeplayMenu" ->
                                Flixel.switchState(FreeplayMenu())
                            else -> {
                                Flixel.warn("Target with id \"${targets[curSelected]}\" does not exist! Ignoring input.")
                                canSelect = true
                                item.visible = true
                            }
                        }
                    }
                })
                canSelect = false
            }
            i++
        }
        if (Controls.BACK && canSelect) {

            var snd = Flixel.sound.play("sounds/menu/cancel.mp3")
            snd.isPersist = true

            Flixel.switchState(TitleState())
        }

        if (Controls.UI_DOWN && canSelect) {
            curSelected++
            curSelected = curSelected.wrap(0, items.size)
            Flixel.sound.play("sounds/menu/scroll.mp3")

            camFollow.setPosition(items[curSelected].midpointX, items[curSelected].midpointY)
        }

        if (Controls.UI_UP && canSelect) {
            curSelected--
            curSelected = curSelected.wrap(0, items.size)
            Flixel.sound.play("sounds/menu/scroll.mp3")

            camFollow.setPosition(items[curSelected].midpointX, items[curSelected].midpointY)
        }


    }


}

