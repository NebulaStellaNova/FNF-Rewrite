package com.nebulastellanova.rewrite.lwjgl3

import org.flixelgdx.backend.lwjgl3.FlixelLwjgl3Launcher
import com.nebulastellanova.rewrite.RewriteGame
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

  FlixelLwjgl3Launcher.launch(RewriteGame(), FlixelRuntimeMode.DEBUG,
      "images/window/icon16.png",
      "images/window/icon32.png",
      "images/window/icon64.png",
      "images/window/icon128.png"
  )
}
