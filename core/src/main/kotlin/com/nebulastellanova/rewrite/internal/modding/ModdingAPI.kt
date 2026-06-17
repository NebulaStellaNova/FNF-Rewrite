package com.nebulastellanova.rewrite.internal.modding

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.IntSet
import com.nebulastellanova.rewrite.util.Constants.Modding.VALID_ID_CHARS
import org.flixelgdx.Flixel
import java.util.Properties
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * Global handler for interacting with the modding API.
 */
object ModdingAPI {
    val enabledMods: IntSet = IntSet()
    val loadedMods: GdxArray<ModMeta> = GdxArray()

    /**
     * Boot up the modding API and read the `mods/` folder.
     */
    fun init() {
        loadMods()
    }

    private fun loadMods() {
        loadedMods.clear()
        val folder = Gdx.files.local("mods")
        if (!folder.exists() || !folder.isDirectory) {
            folder.mkdirs()
        }

        val mods = folder.list()
        if (mods.isEmpty()) {
            Flixel.info("No mods found.")
        } else {
            Flixel.info("Found ${mods.size} mod${if (mods.size != 1) "s" else ""}:")
        }

        for (mod in mods) {
            if (mod.isDirectory) {
                val localFile = Gdx.files.local("mods/${mod.name()}/mod.properties")
                if (localFile.exists()) {
                    Flixel.info("\t${mod.name()}")
                    val properties = Properties()
                    localFile.read().use { stream ->
                        properties.load(stream)
                    }
                    val modMeta = ModMeta(properties)
                    verifyModId(modMeta.id)
                    Flixel.info(modMeta)
                } else {
                    Flixel.warn("Could not find \"mod.properties\" for detected mod \"${mod.name()}\", skipping...")
                }
            }
        }
    }

    private fun verifyModId(id: String) {
        val matches = VALID_ID_CHARS.matches(id)
        if (!matches) {
            throw IllegalArgumentException(
                "ID \"${id}\" is not valid, it must only contain lowercase letters and underscores.",
            )
        }
    }
}

/**
 * Data class for holding metadata of a single mod.
 *
 * @param properties The [Properties] object, which contains the metadata from a mod.
 */
data class ModMeta(
    val properties: Properties,
) {
    /**
     * The ID of the mod. The formatting must match [VALID_ID_CHARS], otherwise an exception is thrown.
     */
    val id: String = properties.getProperty("id") ?: "Unknown"

    /**
     * The human-readable name of the mod.
     */
    val name: String = properties.getProperty("name") ?: "Unknown"

    /**
     * The description of what the mod is about.
     */
    val description: String = properties.getProperty("description") ?: "Unknown"

    /**
     * A list of all contributors who helped with the mod.
     */
    val contributors: Array<String> =
        properties
            .getProperty("contributors")
            ?.split(",")
            ?.map { it.trim() }
            ?.toTypedArray() ?: emptyArray()

    /**
     * A full URL to the GitHub repository of the mod.
     */
    val github: String = properties.getProperty("github") ?: "Unknown"

    /**
     * The released version of the mod.
     */
    val version: String = properties.getProperty("version") ?: "Unknown"

    override fun toString(): String =
        "{ id: $id, name: $name, contributors: ${contributors.contentToString()}, github: $github, version: $version }"
}
