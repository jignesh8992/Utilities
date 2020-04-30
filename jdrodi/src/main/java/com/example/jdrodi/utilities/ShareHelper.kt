package com.example.jdrodi.utilities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.jdrodi.R
import java.io.ByteArrayOutputStream
import java.io.File


private val TAG = ShareHelper::class.java.simpleName


@Suppress("unused")
object ShareHelper {

    /**
     * ToDo.. Share text message
     *
     * @param title    The title of share intent
     * @param body     The body of share intent
     */

    fun Context.shareText(title: String?, body: String?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body)
        this.startActivity(Intent.createChooser(sharingIntent, title))
    }

    /**
     * ToDo.. Share image form Uri of image
     *
     * @param uri      The uri of image
     */
    fun Context.shareImage(uri: Uri) {
        try {
            shareImage(uri.toString(), null)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, e.toString())
        }
    }

    /**
     * ToDo.. Share image form Uri of image
     *
     * @param uri         The uri of image
     * @param packageName The packageName of application is to share image on the app
     */
    fun Context.shareImage(uri: Uri, packageName: String?) {
        try {
            shareImage(uri.toString(), packageName)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, e.toString())
        }
    }

    /**
     * ToDo.. Share image form path of image
     * @param path     The path of image
     */
    fun Context.shareImage(path: String) {
        try {
            shareImage(path, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, e.toString())
        }
    }

    /**
     * ToDo.. Share image form Uri of image
     *
     * @param path        The path of image
     * @param packageName The packageName of application is to share image on the app
     */
    fun Context.shareImage(path: String, packageName: String?) {
        try {
            // if app is installed
            if (packageName != null && this.packageManager.getLaunchIntentForPackage(packageName) == null) {

                val msg = when (packageName) {
                    getString(R.string.package_name_wa) -> getString(R.string.wa_not_installed)
                    getString(R.string.package_name_instagram) -> getString(R.string.instagram_not_installed)
                    else -> getString(R.string.fb_not_installed)
                }
                Toast.short(this, msg)

                // open play store
                val intent = Intent("android.intent.action.VIEW")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse("market://details?id=$packageName")
                this.startActivity(intent)
            } else {
                val contentUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", File(path))
                if (contentUri != null) {
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                    shareIntent.setDataAndType(contentUri, this.contentResolver.getType(contentUri))
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    shareIntent.type = "image/*"
                    if (packageName != null) shareIntent.setPackage(packageName)
                    this.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, e.toString())
        }
    }

    /**
     * ToDo.. Get Uri of Bitmap Image
     *
     * @param bitmap   The image bitmap
     * @return The uri of bitmap image
     */
    @Suppress("DEPRECATION")
    fun Context.getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }
    // TODO VIDEO SHARE BLOCK

    /**
     * ToDo.. Share image form uri of video
     *
     * @param path         The path of video
     * @param packageName The packageName of application is to share image on the app
     */
    fun Context.shareVideo(path: String, packageName: String? = null) {
        try {
            // if app is installed
            if (packageName != null && this.packageManager.getLaunchIntentForPackage(packageName) == null) {
                // if app is not installed
                val msg = when (packageName) {
                    getString(R.string.package_name_wa) -> getString(R.string.wa_not_installed)
                    getString(R.string.package_name_instagram) -> getString(R.string.instagram_not_installed)
                    else -> getString(R.string.fb_not_installed)
                }
                Toast.short(this, msg)

                // open play store
                val intent = Intent("android.intent.action.VIEW")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse("market://details?id=$packageName")
                this.startActivity(intent)
            } else {
                val contentUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", File(path))
                if (contentUri != null) {
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                    shareIntent.setDataAndType(contentUri, this.contentResolver.getType(contentUri))
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    shareIntent.type = "video/*"
                    if (packageName != null) shareIntent.setPackage(packageName)
                    this.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
                }
            }
        } catch (ignored: Exception) {
            Log.i(TAG, ignored.toString())
        }
    }
}