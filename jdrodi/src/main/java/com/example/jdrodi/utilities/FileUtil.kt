@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import java.io.*
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*


/**
 * FileUtil.kt - A simple class to perform operations on file.
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */

object FileHelper

private val TAG = FileHelper::class.java.simpleName
private const val DEFAULT_BUFFER_SIZE = 4096
private const val EOF = -1

/**
 *  Return file size in B, KB, MB, GB format
 *
 * @param size The size to convert
 */
@SuppressLint("DefaultLocale")
fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> String.format("%d B", size)
        size < 1024 * 1024 -> String.format("%.1f KB", size / 1024.0f)
        size < 1024 * 1024 * 1024 -> String.format("%.1f MB", size.toFloat() / 1024.0f / 1024.0f)
        size < 1024 * 1024 * 1024 * 1024 -> String.format("%.1f GB", size.toFloat() / 1024.0f / 1024.0f / 1024.0f)
        else -> String.format("%.1f TB", size.toFloat() / 1024.0f / 1024.0f / 1024.0f / 1024.0f)
    }
}

/**
 *  Check File extension
 *
 * @param path      The path to check extension
 * @param extension The extension to check
 */
fun isExtension(path: String, extension: String): Boolean {
    val currExtension = path.substring(path.lastIndexOf(".") + 1, path.length)
    return extension.equals(currExtension, ignoreCase = true)
}


/**
 * ToDo.. Get name of file
 *
 * @param path The path
 * @return The name of path
 */
fun getFileName(path: String): String? {
    return path.substring(path.lastIndexOf("/") + 1)
}


/**
 *  Load List of files from given directory path
 *
 * @param folder The folder to retrieve list of files
 */
fun loadFilesPath(folder: String): ArrayList<String> {
    val pathList = ArrayList<String>()
    var fileNames: Array<String>? = null
    val path = File(folder)
    if (path.exists()) {
        fileNames = path.list()
    }
    if (fileNames != null) {
        for (i in fileNames.indices) {
            pathList.add(path.path + "/" + fileNames[i])
        }
    }
    return pathList
}

/**
 * ToDo.. Load List of files from given directory path
 *
 * @param folder The folder to retrieve list of files
 */
fun loadFiles(folder: String): ArrayList<File> {
    val pathList = ArrayList<File>()
    var fileNames: Array<String>? = null
    val path = File(folder)
    if (path.exists()) {
        fileNames = path.list()
    }
    if (fileNames != null) {
        for (i in fileNames.indices) {
            pathList.add(File(path.path.toString() + "/" + fileNames[i]))
        }
    }
    return pathList
}


/**
 *  Delete file by path
 *
 * @param path The path to delete
 */
fun deletePath(path: String): Boolean {
    return File(path).delete()
}

/**
 *  Delete file by Uri
 *
 * @param uri  The Uri to delete.
 */
fun Context.deleteUri(uri: Uri): Boolean {
    return File(getPath(uri)!!).delete()
}


/**
 *  Copies one file into the other with the given paths.
 * In the event that the paths are the same, trying to copy one file to the other
 * will cause both files to become null.
 * Simply skipping this step if the paths are identical.
 */
@Throws(IOException::class)
fun copyFile(@NonNull pathFrom: String, @NonNull pathTo: String) {
    if (pathFrom.equals(pathTo, ignoreCase = true)) {
        return
    }

    var outputChannel: FileChannel? = null
    var inputChannel: FileChannel? = null
    try {
        inputChannel = FileInputStream(File(pathFrom)).channel
        outputChannel = FileOutputStream(File(pathTo)).channel
        inputChannel!!.transferTo(0, inputChannel.size(), outputChannel)
        inputChannel.close()
    } finally {
        inputChannel?.close()
        outputChannel?.close()
    }
}


/**
 *  Get a file path from a Uri.
 * This will get the the path for Storage Access Framework Documents, as well as the _data
 * field for the MediaStore and other file-based ContentProviders.
 * Callers should check whether the path is local before assuming it represents a local file.
 *
 * @param uri     The Uri to query.
 */
@Suppress("DEPRECATION")
@SuppressLint("NewApi")
fun Context.getPath(uri: Uri): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            if (!TextUtils.isEmpty(id)) {
                return try {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )
                    getDataColumn(contentUri, null, null)
                } catch (e: NumberFormatException) {
                    Log.i(TAG, e.message!!)
                    null
                }

            }

        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            when (type) {
                "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(contentUri, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

        // Return the remote address
        return if (isGooglePhotosUri(uri)) {
            uri.lastPathSegment
        } else getDataColumn(uri, null, null)

    } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
        return uri.path
    }// File
    // MediaStore (and general)

    return null
}


/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
private fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param uri           The Uri to query.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 * @author Jignehs
 */
private fun Context.getDataColumn(uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } catch (ex: IllegalArgumentException) {
        Log.i(
            TAG, String.format(
                Locale.getDefault(), "getDataColumn: _data - [%s]", ex
                    .message
            )
        )
    } finally {
        cursor?.close()
    }
    return null
}

/**
 *  Check File extension
 *
 * @param name   The name to remove extension
 * @return  The File name without extension
 */

fun removeExtension(name: String): String {
    var newName: String = name
    if (name.indexOf(".") > 0)
        newName = name.substring(0, name.lastIndexOf("."))

    return newName
}


fun Context.createTempUri(): Uri {
    return Uri.fromFile(File(this.cacheDir, "temp"))
}

fun Context.addImageToGallery(filePath: String) {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.MediaColumns.DATA, filePath)
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
}


fun Context.getCameraPhotoOrientation(imagePath: String): Int {
    var rotate = 90
    try {
        val imageFile = File(imagePath)
        val exif = ExifInterface(imageFile.absolutePath)
        when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return rotate
}


fun getFolderName(str: String?): String {
    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str)
    return if (file.exists() || file.mkdirs()) {
        file.absolutePath
    } else ""
}

fun isSDAvailable(): Boolean {
    return Environment.getExternalStorageState() == "mounted"
}

fun Context.getNewFile(str: String?): File? {
    val str2: String
    val format = SimpleDateFormat("" + System.currentTimeMillis(), Locale.CHINA).format(Date())
    str2 = if (isSDAvailable()) {
        getFolderName(str) + File.separator + format + ".jpg"
    } else {
        filesDir.path + File.separator + format + ".jpg"
    }
    return if (TextUtils.isEmpty(str2)) {
        null
    } else File(str2)
}

fun Context.getNewFilePng(str: String?): File? {
    val str2: String
    val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
    str2 = if (isSDAvailable()) {
        getFolderName(str) + File.separator + format + ".png"
    } else {
        filesDir.path + File.separator + format + ".png"
    }
    return if (TextUtils.isEmpty(str2)) {
        null
    } else File(str2)
}

fun saveImageToGallery(file: File, bitmap: Bitmap, bool: Boolean): File {
    try {
        Log.e(TAG, "File: $file")
        val fileOutputStream = FileOutputStream(file)
        Log.e(TAG, " isCompress =  $bool")
        if (!bool) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fileOutputStream)
        }
        fileOutputStream.flush()
        fileOutputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e(TAG, "saveImageToGallery:  $e")
    }
    Log.e(TAG, "saveImageToGallery: the path of bmp is.. " + file.absolutePath + "  " + bool)
    return file
}

fun saveImageToGalleryPng(file: File, bitmap: Bitmap): File? {
    try {
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    Log.e(TAG, "saveImageToGallery: the path of bmp is.. " + file.absolutePath)
    return file
}

fun Context.notifySystemGallery(file: File) {
    if (file.exists()) {
        sendBroadcast(Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)))
        return
    }
    throw java.lang.IllegalArgumentException("bmp should not be null")
}


private fun rename(file: File, str: String): File? {
    val file2 = File(file.parent, str)
    if (file2 != file) {
        if (file2.exists() && file2.delete()) {
            Log.d(TAG, "Delete old $str file")
        }
        if (file.renameTo(file2)) {
            Log.d(TAG, "Rename file to $str")
        }
    }
    return file2
}

@Throws(IOException::class)
private fun copy(inputStream: InputStream, outputStream: OutputStream): Long {
    val bArr = ByteArray(4096)
    var j: Long = 0
    while (true) {
        val read = inputStream.read(bArr)
        if (-1 == read) {
            return j
        }
        outputStream.write(bArr, 0, read)
        j += read.toLong()
    }
}

fun Context.refreshGallery(path: String) {
    MediaScannerConnection.scanFile(this, arrayOf(path), null) { path, uri -> Log.i("8992", "Scanned $path") }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.parse(path) //out is your file you
        // saved/deleted/moved/copied
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)
    } else {
        sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())))
    }
    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(path))))
}
