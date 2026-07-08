package com.nebulastellanova.rewrite.utils

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document

object ParseUtil {
    fun loadXmlFromPath(filePath: String): Document? {
        return try {
            val file = File(filePath)
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            builder.parse(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
