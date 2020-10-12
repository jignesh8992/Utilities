@file:Suppress("unused")

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

/**
 * AssetsHelper.kt - A simple class for assets utils
 * @author  Jignesh N Patel
 * @date 12-10-2020
 */

private const val TAG = "AssetsHelper"

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
 * Get bitmap from the path
 *
 * @param path     The path of image
 * @return The bitmap of given path
 */
fun Context.getAssetBitmap(path: String): Bitmap? {
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
 * Get bitmap from the path
 *
 * @param path     The path of image
 * @return The bitmap of given path
 */
fun getBitmap(mContext: Context, path: String): Bitmap? {
    var stream: InputStream? = null
    try {
        stream = mContext.assets.open(path)
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
 * Get Drawable from the path
 *
 * @param filePath     The path of image
 * @return The drawable of given path
 */
fun Context.getDrawable(filePath: String): Drawable? {
    var stream: InputStream? = null
    try {
        stream = assets.open(filePath)
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
 * Get Assets path from simple path
 *
 * @param filePath     The path of file
 * @return The string
 */
fun Context.getAssetsFilePath(filePath: String): String {
    return "file:///android_asset/$filePath"
}


/**
 * Get String from file
 *
 * @param filePath     The path of file
 * @return The string
 */
fun Context.getJsonDataFromAsset(filePath: String): String? {
    val jsonString: String
    try {
        jsonString = assets.open(filePath).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}
