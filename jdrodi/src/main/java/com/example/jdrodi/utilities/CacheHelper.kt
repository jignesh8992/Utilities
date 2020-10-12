@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * CacheHelper.kt - A simple class for cache utils
 * @author  Jignesh N Patel
 * @date 12-10-2020
 */

private const val TAG = "CacheHelper"
private const val CHILD_DIR = "images"
private const val TEMP_FILE_NAME = "img"
private const val FILE_EXTENSION = ".png"
private const val COMPRESS_QUALITY = 100

/**
 * Save image to the App cache
 *
 * @param bitmap to save to the cache
 * @param name   file name in the cache.
 * If name is null file will be named by default [.TEMP_FILE_NAME]
 * @return file dir when file was saved
 */
fun Context.saveImgToCache(bitmap: Bitmap, name: String?): File? {
    var cachePath: File? = null
    var fileName: String? = TEMP_FILE_NAME
    if (!TextUtils.isEmpty(name)) {
        fileName = name
    }
    try {
        cachePath = File(cacheDir, CHILD_DIR)
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/$fileName$FILE_EXTENSION")
        bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, stream)
        stream.close()
    } catch (e: IOException) {
        Log.e(TAG, "saveImgToCache error: $bitmap", e)
    }
    return cachePath
}

/**
 * Save an image to the App cache dir and return it
 *
 * @param bitmap to save to the cache
 * @param name   file name in the cache.
 * If name is null file will be named by default [.TEMP_FILE_NAME]
 */

@JvmOverloads
fun Context.saveToCacheAndGetUri(bitmap: Bitmap, name: String? = null): Uri {
    val file = saveImgToCache(bitmap, name)
    return getImageUri(file, name)
}

/**
 * Save an image to the App cache dir and return it [Uri]
 *
 * @param bitmap to save to the cache
 * @param name   file name in the cache.
 * If name is null file will be named by default [.TEMP_FILE_NAME]
 */
fun Context.saveToCacheAndGetFile(bitmap: Bitmap, name: String): File {
    val saveLocation = saveImgToCache(bitmap, name)
    return File(saveLocation, name + FILE_EXTENSION)
}

/**
 * Get a file [Uri]
 *
 * @param name of the file
 * @return file Uri in the App cache or null if file wasn't found
 */
fun Context.getUriByFileName(name: String): Uri? {
    val fileName: String = if (!TextUtils.isEmpty(name)) {
        name
    } else {
        return null
    }
    val imagePath = File(cacheDir, CHILD_DIR)
    val newFile = File(imagePath, fileName + FILE_EXTENSION)
    return FileProvider.getUriForFile(this, "$packageName.FileProvider", newFile)
}

// Get an image Uri by name without extension from a file dir
private fun Context.getImageUri(fileDir: File?, name: String?): Uri {
    var fileName: String? = TEMP_FILE_NAME
    if (!TextUtils.isEmpty(name)) {
        fileName = name
    }
    val newFile = File(fileDir, fileName + FILE_EXTENSION)
    return FileProvider.getUriForFile(this, "$packageName.FileProvider", newFile)
}

/**
 * Get Uri type by [Uri]
 */
fun Context.getContentType(uri: Uri?): String? {
    return contentResolver.getType(uri!!)
}