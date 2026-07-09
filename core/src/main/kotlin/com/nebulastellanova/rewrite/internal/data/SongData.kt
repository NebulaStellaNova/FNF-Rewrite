package com.nebulastellanova.rewrite.internal.data

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue

data class SongCharacterData(
    val player: String = "",
    val girlfriend: String = "",
    val opponent: String = "",
    val instrumental: String = "",
    val altInstrumentals: ArrayList<String> = arrayListOf(),
    val opponentVocals: ArrayList<String> = arrayListOf(),
    val playerVocals: ArrayList<String> = arrayListOf()
)

data class SongPlayData(
    val songVariations: ArrayList<String> = arrayListOf(),
    val difficulties: ArrayList<String> = arrayListOf("easy", "normal", "hard"),
    val characters: SongCharacterData = SongCharacterData(),
    val stage: String = "mainStage",
    val noteStyle: String = "funkin",
    val album: String = "placeholder",
    val stickerPack: String = ""
)

data class SongTimeChange(
    /**
     * Timestamp in specified `timeFormat`.
     */
    val t: Float = 0.0f,

    /**
     * Time in beats (int). The game will calculate further beat values based on this one,
     * so it can do it in a simple linear fashion.
     */
    val b: Float = 0.0f,

    /**
     * Quarter notes per minute (float). Cannot be empty in the first element of the list,
     * but otherwise it's optional, and defaults to the value of the previous element.
     */
    val bpm: Double = 0.0,

    /**
     * Time signature numerator (int). Optional, defaults to 4.
     */
    val n: Int = 4,

    /**
     * Time signature denominator (int). Optional, defaults to 4. Should only ever be a power of two.
     */
    val d: Int = 4,

    /**
     * Beat tuplets (Array<int> or int). This defines how many steps each beat is divided into.
     * It can either be an array of length `n` (see above) or a single integer number.
     * Optional, defaults to `[4]`.
     */
    val bt: Array<Int> = arrayOf()
)

enum class SongTimeFormat(val value: String) {
    TICKS("ticks"),
    FLOAT("float"),
    MILLISECONDS("ms")
}

data class SongMeta(
    val version: String = "",
    val songName: String = "Unknown",
    val artist: String = "Unknown",
    val charter: String = "Unknown",
    val divisions: Int = 96,
    // val offsets: Any,
    val looped: Boolean = false,
    val playData: SongPlayData = SongPlayData(),
    val generatedBy: String = "Unknown",
    val timeFormat: SongTimeFormat = SongTimeFormat.MILLISECONDS,
    val timeChanges: Array<SongTimeChange> = arrayOf(),
    val variation: String = ""
)


data class SongNoteData(
    /**
     * The timestamp of the note. The timestamp is in the format of the song's time format.
     */
    val t: Float = 0.0f,

    /**
     * Data for the note. Represents the index on the strumline.
     * 0 = left, 1 = down, 2 = up, 3 = right
     * `floor(direction / strumlineSize)` specifies which strumline the note is on.
     * 0 = player, 1 = opponent, etc.
     */
    val d: Int = 0,

    /**
     * Length of the note, if applicable.
     * Defaults to 0 for single notes.
     */
    val l: Float = 0.0f,

    /**
     * The kind of the note.
     * This can allow the note to include information used for custom behavior.
     * Defaults to `null` for no kind.
     */
    val k: String = ""
)

data class SongEventData (
    /**
     * The timestamp of the event. The timestamp is in the format of the song's time format.
     */
    var t: Float = 0.0f,

    /**
     * The kind of the event.
     * Examples include "FocusCamera" and "PlayAnimation"
     * Custom events can be added by scripts with the `ScriptedSongEvent` class.
     */
    var e: String = "",

    /**
     * The data for the event.
     * This can allow the event to include information used for custom behavior.
     * Data type depends on the event kind. It can be anything that's JSON serializable.
     */
    var v: Any? = null,

    /**
     * Whether this event has been activated.
     * This is only used internally by the game during gameplay. It should not be serialized.
     */
    var activated: Boolean = false
) : Json.Serializable {

    override fun write(json: Json) {
        json.writeValue("t", t)
        json.writeValue("e", e)
        json.writeValue("v", v)
        // activated is intentionally not written (transient/internal)
    }

    override fun read(json: Json, jsonMap: JsonValue) {
        t = jsonMap.getFloat("t", 0f)
        e = jsonMap.getString("e", "")
        val vNode = jsonMap.get("v")
        v = if (vNode == null || vNode.isNull) null else json.readValue<Any>(null, vNode)
        //                                                            ^^^^^ pin T to Any
    }
}

data class SongChartData(
    val scrollSpeed: LinkedHashMap<String, Float> = linkedMapOf("easy" to 3f, "normal" to 3f, "hard" to 3f),
    val events: Array<SongEventData> = arrayOf(),
    val notes: LinkedHashMap<String, Array<SongNoteData>> = linkedMapOf(
        "easy" to arrayOf(SongNoteData()),
        "normal" to arrayOf(SongNoteData()),
        "hard" to arrayOf(SongNoteData())
    ),
    val variation: String = ""
)
