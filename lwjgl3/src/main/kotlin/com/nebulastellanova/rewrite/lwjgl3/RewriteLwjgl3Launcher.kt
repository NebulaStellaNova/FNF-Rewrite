package com.nebulastellanova.rewrite.lwjgl3

import com.nebulastellanova.rewrite.FunkinGame
import org.flixelgdx.backend.lwjgl3.FlixelLwjgl3Launcher
import org.flixelgdx.backend.runtime.FlixelRuntimeMode

/**
 * Desktop entry point.
 *
 * `FlixelLwjgl3Launcher` wires libGDX, logging, and window events for you. The [StartupHelper] (same package)
 * restarts the JVM on macOS and NVIDIA Linux when needed.
 *
 * If you're new, don't worry about this file too much; focus on the `core` folder, as that's where your
 * game's code will live.
 */
fun main() {
    if (StartupHelper.startNewJvmIfRequired()) {
        return
    }

    val game = FunkinGame()
    val config = FlixelLwjgl3Launcher.buildDefaultConfig(game)
    config.disableAudio(true)
    FlixelLwjgl3Launcher.launch(game, FlixelRuntimeMode.DEBUG, config)
}
