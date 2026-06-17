package com.nebulastellanova.rewrite.internal.modding

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import org.flixelgdx.Flixel
import com.badlogic.gdx.utils.Array as GdxArray
import java.util.Properties

object ModdingAPI {

    val loadedMods: GdxArray<String> = GdxArray()
    val enabledMods: GdxArray<String> = GdxArray()

    fun init() {
        val folder: FileHandle = Gdx.files.local("mods")
        Flixel.info("Found Mods:")
        for (file in folder.list()) {
            if (file.isDirectory) {
                val localFile: FileHandle = Gdx.files.local("mods/${file.name()}/mod.properties")
                if (localFile.exists()) {
                    Flixel.info("\t${file.name()}")
                    val properties = Properties()
                    localFile.read().use { stream ->
                        properties.load(stream)
                    }
                    val modMeta = ModMeta(properties)
                    Flixel.info(modMeta)
                } else {
                    Flixel.info("\tCould not find \"mod.properties\" in \"${file.name()}\" mod folder.")
                }
            }
        }
    }
}

class ModMeta(properties: Properties) {
    var id: String? = null
    var name: String? = null
    var description: String? = null
    val contributors: GdxArray<String> = GdxArray()
    var github: String? = null
    var version: String? = null

    init {
        id = properties.getProperty("id")
        name = properties.getProperty("name")
        description = properties.getProperty("description")
        for (contributor in properties.getProperty("contributors").split(",")) {
            contributors.add(contributor.trim())
        }
        github = properties.getProperty("github")
        version = properties.getProperty("version")
    }

    override fun toString(): String {
        return "{ id: $id, name: $name, contributors: $contributors, github: $github, version: $version }"
    }
}
