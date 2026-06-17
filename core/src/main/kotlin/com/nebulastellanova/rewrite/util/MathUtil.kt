package com.nebulastellanova.rewrite.util

object MathUtil {

    fun Float.wrap(min: Float, max: Float): Float {
        if (this < min) return max - 1
        if (this > max - 1) return min
        return this
    }

    fun Int.wrap(min: Int, max: Int): Int {
        if (this < min) return max - 1
        if (this > max - 1) return min
        return this
    }

}
