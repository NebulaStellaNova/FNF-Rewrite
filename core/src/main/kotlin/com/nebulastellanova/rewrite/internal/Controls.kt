package com.nebulastellanova.rewrite.internal

import org.flixelgdx.Flixel
import org.flixelgdx.input.keyboard.FlixelKey

object Controls {

    var keybinds = mapOf(
        "ui-left" to arrayListOf("LEFT", "A"),
        "ui-down" to arrayListOf("DOWN", "S"),
        "ui-up" to arrayListOf("UP", "W"),
        "ui-right" to arrayListOf("RIGHT", "D"),
        "accept" to arrayListOf("ENTER", "Z"),
        "back" to arrayListOf(/* "BACKSPACE", */ "ESCAPE"),

        "note-left" to arrayListOf("LEFT", "W"),
        "note-down" to arrayListOf("LEFT", "F"),
        "note-up" to arrayListOf("LEFT", "J"),
        "note-right" to arrayListOf("LEFT", "O")
    )

    val NOTE_LEFT: Boolean
            get() { return get("note-left") }
    val NOTE_DOWN: Boolean
            get() { return get("note-down") }
    val NOTE_UP: Boolean
            get() { return get("note-up") }
    val NOTE_RIGHT: Boolean
            get() { return get("note-right") }

    val UI_LEFT: Boolean
        get() { return get("ui-left") }

    val UI_DOWN: Boolean
        get() { return get("ui-down") }

    val UI_UP: Boolean
        get() { return get("ui-up") }

    val UI_RIGHT: Boolean
        get() { return get("ui-right") }

    val ACCEPT: Boolean
        get() { return get("accept") }

    val BACK: Boolean
        get() { return get("back") }

    fun get(input: String): Boolean {
        for (value in keybinds[input]!!) {
            if (Flixel.keys.justPressed(FlixelKey.fromString(cap(value)))) {
                return true
            }
        }
        return false
    }

    fun cap(string: String): String {
        return string.substring(0, 1).uppercase() + string.substring(1).lowercase()
    }
}
