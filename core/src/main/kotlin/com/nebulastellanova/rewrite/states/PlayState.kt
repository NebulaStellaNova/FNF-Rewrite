package com.nebulastellanova.rewrite.states

import com.badlogic.gdx.graphics.Color
import com.nebulastellanova.rewrite.internal.Constants
import com.nebulastellanova.rewrite.internal.FunkinConductor
import com.nebulastellanova.rewrite.internal.data.SongChartData
import com.nebulastellanova.rewrite.internal.data.SongMeta
import com.nebulastellanova.rewrite.objects.NoteSprite
import com.nebulastellanova.rewrite.objects.Strumline
import com.nebulastellanova.rewrite.utils.ParseUtil
import org.flixelgdx.Flixel
import org.flixelgdx.FlixelCamera
import org.flixelgdx.FlixelState
import org.flixelgdx.audio.FlixelSound

class PlayState : FlixelState() {

    companion object {
        var songID: String = "bopeebo"
        var variation: String = "erect"
        var difficulty: String = "nightmare"
        var conductor: FunkinConductor = FunkinConductor();
        var scrollSpeed: Float = 1f
    }

    var inst: FlixelSound? = null


    var playerVocals: Array<FlixelSound> = arrayOf()
    var opponentVocals: Array<FlixelSound> = arrayOf()

    var notes:Array<NoteSprite> = arrayOf()

    lateinit var playerStrumline: Strumline
    lateinit var opponentStrumline: Strumline

    var camGame: FlixelCamera = FlixelCamera()
    var camHUD: FlixelCamera = FlixelCamera()


    override fun create() {
        super.create()
        conductor = FunkinConductor();
        TitleState.menuTheme?.track?.stop()
        var suffix = if (variation != "") "-$variation" else ""
        inst = ParseUtil.loadAudioFromZip("songs/$songID.fnfc", "Inst$suffix.ogg")

        camHUD.bgColor = Color(0f, 0f, 0f, 0f)

        Flixel.cameras.add(camGame)
        Flixel.cameras.add(camHUD)

        if (inst != null) {
            conductor.track = inst
        }

        val jsonDataBytes = ParseUtil.getFileFromZip("songs/$songID.fnfc", "$songID-metadata$suffix.json")
        val jsonChartDataBytes = ParseUtil.getFileFromZip("songs/$songID.fnfc", "$songID-chart$suffix.json")

        if (jsonDataBytes != null) {
            val jsonString = String(jsonDataBytes, Charsets.UTF_8)
            val data: SongMeta = ParseUtil.parseJsonString(jsonString)
            conductor.bpm = data.timeChanges[0].bpm

            data.playData.characters.playerVocals += data.playData.characters.player
            data.playData.characters.playerVocals.forEach { i ->
                val vocal = ParseUtil.loadAudioFromZip("songs/$songID.fnfc", "Voices-$i$suffix.ogg")
                if (vocal != null) {
                    conductor.addAdditionalTrack(vocal)
                    playerVocals += vocal
                }
            }

            data.playData.characters.opponentVocals += data.playData.characters.opponent
            data.playData.characters.opponentVocals.forEach { i ->
                val vocal = ParseUtil.loadAudioFromZip("songs/$songID.fnfc", "Voices-$i$suffix.ogg")
                if (vocal != null) {
                    conductor.addAdditionalTrack(vocal)
                    opponentVocals += vocal
                }
            }
        }
        if (jsonChartDataBytes != null) {
            val jsonString = String(jsonChartDataBytes, Charsets.UTF_8)
            val data: SongChartData = ParseUtil.parseJsonString(jsonString)
            if (data.notes[difficulty] != null) {
                playerStrumline = Strumline(data.notes[difficulty]!!.filter { it.d <= 3 })
                opponentStrumline = Strumline(data.notes[difficulty]!!.filter { it.d > 3 })
            }
            scrollSpeed = data.scrollSpeed[difficulty]!!
        }

        playerStrumline.cameras = arrayOf(camHUD)
        playerStrumline.x = (1280/2) + Constants.STRUMLINE_X_OFFSET
//        playerStrumline.cpu = false
        opponentStrumline.cameras = arrayOf(camHUD)
        opponentStrumline.x = Constants.STRUMLINE_X_OFFSET
        playerStrumline.y = 720f-180
        opponentStrumline.y = 720f-180

        add(opponentStrumline)
        add(playerStrumline)

        add(conductor);
        conductor.play()

    }

    override fun update(elapsed: Float) {
        super.update(elapsed)

        for (i in notes) {
            i.y = (conductor.songPosition - i.time).toFloat() + 620
        }
    }
}
