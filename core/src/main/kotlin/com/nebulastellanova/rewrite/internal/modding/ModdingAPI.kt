package com.nebulastellanova.rewrite.internal.modding

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import java.util.Properties

object ModdingAPI {

    var loadedMods = arrayListOf<String>() // All the mods available to enable
    var enabledMods = arrayListOf<String>() // All the mods currently enabled

//    fun applyPath(path: String): String {

//    }

    fun init() {
        var folder: FileHandle = Gdx.files.local("mods")
        println("Found Mods:")
        for (file in folder.list()) {
            if (file.isDirectory) {
                val localFile: FileHandle = Gdx.files.local("mods/${file.name()}/mod.properties")
                if (localFile.exists()) {
                    println("\t${file.name()}")
                    var properties = Properties()
                    localFile.read().use { stream ->
                        properties.load(stream)
                    }
                    val modMeta: ModMeta = ModMeta(properties)
                    println(modMeta)
                } else {
                    println("\tCould not find \"mod.properties\" in \"${file.name()}\" mod folder.")
                }
            }
        }
    }
}

class ModMeta(properties: Properties) {
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var contributors = arrayListOf<String>()
    var github: String? = null
    var version: String? = null

    init {
        id = properties.getProperty("id")
        name = properties.getProperty("name")
        description = properties.getProperty("description")
        for (i in properties.getProperty("contributors").split(",")) {
            contributors.add(i.trim())
        }
        github = properties.getProperty("github")
        version = properties.getProperty("version")
    }

    override fun toString(): String {
        return "{ id: $id, name: $name, contributors: $contributors, github: $github, version: $version }"
    }
}
