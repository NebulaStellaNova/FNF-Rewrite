package com.nebulastellanova.rewrite.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Json
import com.nebulastellanova.rewrite.internal.Constants
import com.nebulastellanova.rewrite.internal.modding.ModMeta
import org.flixelgdx.Flixel
import org.flixelgdx.audio.FlixelSound
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Properties
import java.util.zip.ZipInputStream
import javax.xml.parsers.DocumentBuilderFactory

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

    fun getStringFromPath(filePath: String): String {
        try {
            val fileHandle = Gdx.files.internal(filePath)
            val textContent: String = fileHandle.readString()
            return textContent
        } catch (e: Exception) {
            return ""
        }
    }

    fun getFileFromZip(zipPath: String, targetFileName: String): ByteArray? {
        val fileHandle = Gdx.files.internal(zipPath)
        ZipInputStream(fileHandle.read()).use { zipInputStream ->
            var zipEntry = zipInputStream.nextEntry

            while (zipEntry != null) {
                if (!zipEntry.isDirectory && zipEntry.name == targetFileName) {
                    return zipInputStream.readAllBytes()
                }
                zipInputStream.closeEntry()
                zipEntry = zipInputStream.nextEntry
            }
        }
        return null
    }

    inline fun <reified T> parseJsonFromPath(filePath: String): T {
        val string = getStringFromPath(filePath)
        return parseJsonString(string)
    }

    inline fun <reified T> parseJsonString(string: String): T {
        val json = Json().apply {
            setIgnoreUnknownFields(true)
        }
        return json.fromJson(T::class.java, string)
    }

    fun loadAudioFromZip(zipFilePath: String, targetFileName: String): FlixelSound? {
        val zipFile = Gdx.files.internal(zipFilePath)
        if (!zipFile.exists()) return null

        val tempDir = java.nio.file.Files.createTempDirectory("extracted_audio_").toFile()

        val finalSound: FlixelSound? = ZipInputStream(zipFile.read()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val currentFileName = File(entry.name).name
                if (!entry.isDirectory && currentFileName.equals(targetFileName, ignoreCase = true)) {
                    val outputFile = File(tempDir, currentFileName)

                    BufferedOutputStream(FileOutputStream(outputFile)).use { bos ->
                        zis.copyTo(bos)
                    }

                    val loadedSound = Flixel.sound.play(outputFile.absolutePath)
                    loadedSound.stop()

                    zis.closeEntry()
                    return@use loadedSound
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
            null
        }

        tempDir.deleteRecursively()
        return finalSound
    }

}
