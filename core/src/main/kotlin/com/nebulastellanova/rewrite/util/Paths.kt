package com.nebulastellanova.rewrite.util

/**
 * Helper class to shorten directory paths.
 */
object Paths {
    fun image(path: String): String = "images/$path.png"

    fun image(
        path: String,
        ext: String?,
    ): String = "images/$path.$ext"

    fun sparrow(path: String): String = image(path, "xml")
}
