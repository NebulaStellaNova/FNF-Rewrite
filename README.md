# Rewrite

Welcome! This folder is a tiny, runnable FlixelGDX starter. The big idea is simple:
your gameplay code lives in the `core` module, and the `lwjgl3` (or `teavm`) module is only the launcher.

This zip was created by the project generator at
https://flixelgdx.org/getting-started.

## 1. Install a JDK

This project is configured for **Eclipse Temurin (Adoptium)**.
Downloads: https://adoptium.net/temurin/releases/?package=jdk

Quick-start by OS:
  - Windows : run the .msi installer (it sets JAVA_HOME + PATH).
  - macOS   : brew install --cask temurin@17
  - Ubuntu  : add the official Adoptium apt repo, then `apt install temurin-17-jdk`.

Confirm the install: `java -version` in a fresh terminal.

> Any JDK 8+ on PATH is enough to bootstrap Gradle. The build
> auto-downloads the **Eclipse Temurin (Adoptium)** toolchain via Gradle's
> Foojay Toolchains Resolver on first run.

## 2. Open in IntelliJ IDEA

Download: https://www.jetbrains.com/idea/download/

  1. Open IntelliJ -> "Open" -> pick this project folder.
  2. Accept the Gradle import prompt. IntelliJ uses the bundled wrapper.
  3. Run the game via the Gradle tool window (`:lwjgl3:run` for desktop, `:teavm:run` for web).


## 3. Run your game

**Desktop:**

    ./gradlew :lwjgl3:run        # macOS / Linux
    gradlew.bat :lwjgl3:run      # Windows


### GraalVM Native Image

The `lwjgl3` module applies the GraalVM Native Build Tools plugin. With a GraalVM-capable JDK available to Gradle, you can build a standalone executable:

    ./gradlew :lwjgl3:nativeCompile

Native Image needs reflection and resource configuration for libGDX. Expect to iterate with tracing or hand-written reachability metadata as your game grows.

### Construo desktop bundles

The `lwjgl3` module also applies Construo. After a normal `./gradlew :lwjgl3:jar`, you can build cross-platform zips (each target downloads its own JDK bundle once):

    ./gradlew :lwjgl3:packageLinuxX64
    ./gradlew :lwjgl3:packageMacOsAarch64
    ./gradlew :lwjgl3:packageWinX64

Output zips land in the `dist/` folder at the project root.


This project ships the Gradle 9.5.1 wrapper (see `gradle/wrapper/gradle-wrapper.properties`).
Gradle wrapper scripts are bundled. You do not need to install Gradle separately.
First build downloads dependencies (and, if needed, the JDK).

### If Gradle reports an unknown plugin / unresolved plugin artifact

FlixelGDX publishes its Gradle plugins to Maven Central with proper plugin markers,
so `settings.gradle` applies them directly by ID and version. If a plugin fails to
resolve, confirm `mavenCentral()` is present in the `pluginManagement` repositories.
For more detail, see
[COMPILING.md](https://github.com/flixelgdx/flixelgdx/blob/master/COMPILING.md)
in the framework repo.

## What you picked

  - Language        : kotlin
  - Java target     : 17
  - JDK vendor      : Eclipse Temurin (Adoptium)
  - FlixelGDX       : 0.4.2 (pulled from Maven Central)
  - Default heap    : 4096 MB
  - Platforms       : desktop
  - Template        : Blank play state
  - IDE             : IntelliJ IDEA

## Project layout

    core/         gameplay code (states, sprites, logic)
    assets/       shared art, audio, and data (starts empty with .gitkeep so Git keeps the folder)
    lwjgl3/       desktop launcher (LWJGL3 / OpenGL)

    .editorconfig formatting rules (2-space indent, LF, and trailing whitespace trim)
    gradlew[.bat] Gradle wrapper bootstrap

## Native image (optional)

If you enabled GraalVM native image support (`enableGraalNative=true`), you can compile
your game to a standalone binary with no JVM required.

    ./gradlew :lwjgl3:nativeCompile

The binary lands in `lwjgl3/build/native/nativeCompile/`.

### Adding libraries that use JNI or reflection

FlixelGDX's built-in libraries (imgui, miniaudio) are already configured. If you add a
third-party library that uses JNI or reflection, you need to record what it accesses:

  1. Run the config generator:

         ./gradlew :lwjgl3:generateNativeConfig

  2. Play through every feature in your game that the new library is involved in.
  3. Close the game window. The recorded configuration is automatically merged into
     `lwjgl3/src/main/resources/META-INF/native-image/` and picked up by the next build.

The generator only records code paths that actually execute, so make sure to exercise
every screen and feature that touches the library during step 2.

## Learn more

  - Pong tutorial : https://flixelgdx.org/docs/your-first-project
  - API reference : https://flixelgdx.org/api/
  - GitHub        : https://github.com/flixelgdx/flixelgdx
