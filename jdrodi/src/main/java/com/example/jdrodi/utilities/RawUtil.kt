package com.example.jdrodi.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Field
import java.util.*

private const val TAG = "JNP__RawUtil"

/*
 * Todo.. Get List of resources id from raw folder
 *
 * @return ArrayList<RAW> of raw files if exists,or NULL
 */
fun list(): ArrayList<RAW> {
    val ringtone_list = ArrayList<RAW>()
    val fields = arrayOfNulls<Field>(0)
    for (i in fields.indices) {
        try {
            val res_id = fields[i]!!.getInt(fields[i]) // file id
            val res_name = fields[i]!!.name // file name
            val raw = RAW(res_id, res_name)
            ringtone_list.add(raw)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            Log.d(TAG, e.toString())
        }
    }
    return ringtone_list
}

/**
 * ToDo.. Get raw file id by it's name
 *
 * @param mContext The Context
 * @param name     The name of raw file
 * @return Id of raw file
 */
fun Context.getId(name: String?): Int {
    return this.resources.getIdentifier(name, "raw", this.packageName)
}

/**
 * ToDo.. Get raw file name by it's id
 *
 * @param mContext The Context
 * @param id       The id of raw file
 * @return Name of raw file
 */
fun Context.getName(id: Int): String {
    val name = this.resources.getResourceName(id)
    return name.substring(name.lastIndexOf("/") + 1)
}

/**
 * ToDo.. Get uri of raw file by it's id
 *
 * @param mContext The Context
 * @param id       The id of raw file
 * @return uri of raw file
 */
fun Context.getUri(id: Int): Uri {
    return getRawUri(getName(id))
}

/**
 * ToDo.. Get uri of raw file by it's name
 *
 * @param name     The id of raw file
 * @return uri of raw file
 */
fun Context.getRawUri(name: String): Uri {
    return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator + this.packageName + "/raw/" + name)
}

/*
 * ToDo.. Copy raw file into SDCard
 */
@Throws(IOException::class)
fun Context.copyRAWtoSDCard(name: String?, path: String, extension: String): String {
    return copyRAWtoSDCard(getId(name), path, extension)
}

/*
 * ToDo.. Copy raw file into SDCard
 */
@Throws(IOException::class)
fun Context.copyRAWtoSDCard(id: Int, path: String, extension: String): String {
    val outputPath = path + File.separator + getName(id) + extension
    val `in` = this.resources.openRawResource(id)
    val out = FileOutputStream(outputPath)
    val buff = ByteArray(1024)
    var read = 0
    try {
        while (`in`.read(buff).also { read = it } > 0) {
            out.write(buff, 0, read)
        }
    } finally {
        `in`.close()
        out.close()
    }

    return outputPath
}

data class RAW(private var _ID: Int, private var _NAME: String?)