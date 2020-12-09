package com.example.jdrodi.utilities

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.io.IOException

/**
 * ToDo.. Set wallpaper
 *
 *
 * Required permission <uses-permission android:name="android.permission.SET_WALLPAPER"></uses-permission>
 *
 * @param uri      The uri Of image
 */
@SuppressLint("MissingPermission")
fun Context.setWallpaper(uri: Uri) {
    setWallpaper(uri.path!!)
}

/**
 * ToDo.. Set wallpaper
 *
 *
 * Required permission <uses-permission android:name="android.permission.SET_WALLPAPER"></uses-permission>
 *
 * @param path     The Path Of image
 */
@SuppressLint("MissingPermission")
fun Context.setWallpaper(path: String) {
    val imgPath = File(path)
    val bitmap = BitmapFactory.decodeFile(imgPath.absolutePath)
    setWallpaper(this, bitmap)
}

/**
 * ToDo.. Set wallpaper
 *
 *
 * Required permission <uses-permission android:name="android.permission.SET_WALLPAPER"></uses-permission>
 *
 * @param bitmap   The bitmap Of image
 */
@SuppressLint("MissingPermission")
fun setWallpaper(mContext: Context, bitmap: Bitmap) {
    try {
        WallpaperManager.getInstance(mContext).setBitmap(bitmap)
        Toast.makeText(mContext, "Image set into Wallpaper Successfully", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}