package com.example.jdrodi.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*

private val TAG = AssetsHelper::class.java.simpleName

@Suppress("unused")
object AssetsHelper {

    /**
     * Get total size of given directory
     *
     * @param path     The path of directory
     * @return The total size of files of given directory
     */

    fun Context.listSize(path: String): Int {
        try {
            return assets.list(path)!!.size
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        }

        return 0
    }


    /**
     * Get total files of given directory
     *
     * @param path     The path of directory
     * @return The list of files of given directory
     */
    fun Context.listOfFiles(path: String): ArrayList<String> {
        val listOfFiles = ArrayList<String>()
        try {
            // to get all item in give path folder.
            val files = assets.list(path)
            // the loop read all files in give path folder and add into arraylist
            for (i in files!!.indices) {
                listOfFiles.add(path + File.separator + files[i])
            }
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        }

        return listOfFiles
    }

    /**
     * Get bitmap
     *
     * @param path     The path of image
     * @return The bitmap of given path
     */
    fun Context.getBitmap(path: String): Bitmap? {
        var stream: InputStream? = null
        try {
            stream = assets.open(path)
            return BitmapFactory.decodeStream(stream)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
            try {
                stream?.close()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }

        }
        return null
    }

    /**
     * Get bitmap
     *
     * @param path     The path of image
     * @return The drawable of given path
     */
    fun Context.getDrawable(path: String): Drawable? {
        var stream: InputStream? = null
        try {
            stream = assets.open(path)
            return Drawable.createFromStream(stream, null)
        } catch (ignored: Exception) {
        } finally {
            try {
                stream?.close()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }

        }
        return null
    }


    /**
     * Get String from file
     *
     * @param path     The path of file
     * @return The string
     */
    fun Context.getString(path: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open(path)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return json
    }


    fun Context.getAssetsFilePath(path: String): String {
        return "file:///android_asset/$path"
    }
}
