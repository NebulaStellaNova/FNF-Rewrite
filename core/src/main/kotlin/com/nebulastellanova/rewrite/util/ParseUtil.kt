package com.nebulastellanova.rewrite.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.XmlReader

/**
 * Utilities for parsing game data files.
 */
object ParseUtil {
    private val xmlReader = XmlReader()

    /**
     * Parses an XML file from the internal assets directory and returns the root element.
     *
     * @param path the path to the XML file, relative to the assets root (e.g. "data/config/mainmenu.xml").
     * @return the root [XmlReader.Element], or null if parsing fails.
     */
    fun loadXml(path: String): XmlReader.Element? =
        try {
            xmlReader.parse(Gdx.files.internal(path))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}
