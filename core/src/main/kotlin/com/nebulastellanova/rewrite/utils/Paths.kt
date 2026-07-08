package com.nebulastellanova.rewrite.utils

object Paths {
    fun image(path: String): String {
        return "images/$path.png"
    }

    fun image(path: String, ext: String?): String {
        return "images/$path.$ext"
    }

    fun sparrow(path: String): String {
        return image(path, "xml")
    }
}
