@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.*
import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.PorterDuff.Mode
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.core.view.drawToBitmap
import com.example.jdrodi.R
import com.example.jdrodi.utilities.FileHelper.createTempUri
import java.io.*
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * BitmapHelper.kt - A simple class for bitmap utils
 * @author  Jignesh N Patel
 * @date 12-10-2020
 */

private const val TAG = "BitmapHelper"
const val FLIP_VERTICAL = 1
const val FLIP_HORIZONTAL = 2

/**
 * ToDO.. Return bitmap of view
 *
 * @param view The view to be converted into bitmap
 * @return The bitmap of view
 */
fun getBitmap(view: View): Bitmap? {
    return view.drawToBitmap()
}


/**
 * ToDO.. Return bitmap of view
 *
 * @param view The view to be converted into bitmap
 * @return The bitmap of view
 */
@Suppress("DEPRECATION")
fun getBitmapOld(view: View): Bitmap? {
    view.clearFocus()
    view.isPressed = false

    val willNotCache = view.willNotCacheDrawing()
    view.setWillNotCacheDrawing(false)

    // Reset the drawing cache background color to fully transparent
    // for the duration of this operation
    val color = view.drawingCacheBackgroundColor
    view.drawingCacheBackgroundColor = 0

    if (color != 0) {
        view.destroyDrawingCache()
    }
    view.buildDrawingCache()
    val cacheBitmap = view.drawingCache
    if (cacheBitmap == null) {
        Log.e(TAG, "failed getViewBitmap($view)", RuntimeException())
        return null
    }
    val bitmap = Bitmap.createBitmap(cacheBitmap)
    // Restore the view
    view.destroyDrawingCache()
    view.setWillNotCacheDrawing(willNotCache)
    view.drawingCacheBackgroundColor = color
    return bitmap
}


/**
 * ToDO.. Return bitmap of view
 *
 * @param view The view to be converted into bitmap
 * @return The bitmap of view
 */
fun getCanvasBitmap(view: View): Bitmap {
    //Get the dimensions of the view so we can re-layout the view at its current size
    //and create a bitmap of the same size
    val width = view.width
    val height = view.height

    val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
    val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

    //Cause the view to re-layout
    view.measure(measuredWidth, measuredHeight)
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)

    //Create a bitmap backed Canvas to draw the view into
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)

    //Now that the view is laid out and we have a canvas, ask the view to draw itself into
    // the canvas
    view.draw(c)

    return b
}


/**
 * ToDO.. Return bitmap of view
 *
 * @param uri      The uri to be converted into bitmap
 * @return The bitmap of uri
 */
fun Context.getBitmap(uri: Uri): Bitmap? {
    var image: Bitmap? = null
    try {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e(TAG, e.toString())
    }

    return image
}


/**
 *  Save Bitmap in internal storage
 * Required Permission
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
 *
 * @param bitmap   The bitmap to be save in internal storage
 * @return The path of image whee image wil be saved
 */

@Suppress("DEPRECATION")
fun Context.save(bitmap: Bitmap): String {
    val storagePath = createTempUri()
    val name = getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".png"
    return save(bitmap, storagePath.path!!, name)
}


/**
 *  Save Bitmap in internal storage
 * Required Permission
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
 *
 * @param bitmap   The bitmap to be save in internal storage
 * @param name     The name of image
 * @return The path of image whee image wil be saved
 */

@Suppress("DEPRECATION")
fun Context.save(bitmap: Bitmap, name: String): String {
    val storagePath = createTempUri()
    return save(bitmap, storagePath.path!!, name)
}


/**
 *  Save Bitmap in internal storage
 * Required Permission
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
 *
 * @param bitmap   The bitmap to be save in internal storage
 * @param storage_path     The path where bitmap image will be save
 * @param name     The name of image
 * @return The path of image whee image wil be saved
 */

fun Context.save(bitmap: Bitmap, storage_path: String, name: String): String {
    val myDir = File(storage_path)
    myDir.mkdirs()
    val file = File(myDir, name)
    val filepath = file.path
    if (file.exists()) {
        file.delete()
    }
    try {
        if (!file.exists()) {
            file.createNewFile()
        }
        val ostream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream)
        ostream.close()

    } catch (e: Exception) {
        // log(e.getMessage().toString());
    } finally {
        val mediaScanIntent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
        mediaScanIntent.data = Uri.fromFile(File(filepath))
        sendBroadcast(mediaScanIntent)
    }
    return file.path
}


/**
 *  Get center crop bitmap by given high and width
 *
 * @param bitmap    The source bitmap to be center crop
 * @param newHeight The height to be crop
 * @param newWidth  The width to be crop
 * @return The center crop bitmap
 */
fun scaleCenterCropBitmap(bitmap: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
    val sourceWidth = bitmap.width
    val sourceHeight = bitmap.height

    val xScale = newWidth.toFloat() / sourceWidth
    val yScale = newHeight.toFloat() / sourceHeight
    val scale = xScale.coerceAtLeast(yScale)

    val scaledWidth = scale * sourceWidth
    val scaledHeight = scale * sourceHeight

    val left = (newWidth - scaledWidth) / 2
    val top = (newHeight - scaledHeight) / 2

    val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

    val centerCrop = Bitmap.createBitmap(
        newWidth, newHeight,
        bitmap.config
    )
    val canvas = Canvas(centerCrop)
    canvas.drawBitmap(bitmap, null, targetRect, null)

    return centerCrop
}


/**
 *  Resize bitmap by given high and width
 *
 * @param bitmap    The source bitmap to be resize
 * @param newHeight The height to be crop
 * @param newWidth  The width to be crop
 * @return The resized bitmap
 */
fun resizeBitmap(bitmap: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
    val wr = newHeight.toFloat()
    val hr = newWidth.toFloat()
    var wd = bitmap.width.toFloat()
    var he = bitmap.height.toFloat()
    val rat1 = wd / he
    val rat2 = he / wd
    if (wd > wr) {
        wd = wr
        he = wd * rat2
        if (he > hr) {
            he = hr
            wd = he * rat1
        } else {
            wd = wr
            he = wd * rat2
        }
    } else if (he > hr) {
        he = hr
        wd = he * rat1
        if (wd > wr) {
            wd = wr
            he = wd * rat2
        }
    } else if (rat1 > 0.75f) {
        wd = wr
        he = wd * rat2
    } else if (rat2 > 1.5f) {
        he = hr
        wd = he * rat1
    } else {
        wd = wr
        he = wd * rat2
    }
    return Bitmap.createScaledBitmap(bitmap, wd.toInt(), he.toInt(), false)
}


/**
 * Getting bitmap from Assets folder
 *
 * @return
 */
fun Context.getBitmapFromAssets(fileName: String?, width: Int, height: Int): Bitmap? {
    try {
        val assetManager = assets
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val istr = assetManager.open(fileName!!)
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(istr, null, options)
    } catch (e: IOException) {
        Log.e(TAG, "Exception: " + e.message)
    }
    return null
}

/**
 * Getting bitmap from Gallery
 *
 * @return
 */
fun Context.getBitmapFromGallery(path: Uri?, width: Int, height: Int): Bitmap? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(path!!, filePathColumn, null, null, null)
    cursor!!.moveToFirst()
    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
    val picturePath = cursor.getString(columnIndex)
    cursor.close()
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(picturePath, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, width, height)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(picturePath, options)
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight
            && halfWidth / inSampleSize >= reqWidth
        ) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

fun decodeSampledBitmapFromResource(res: Resources?, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap? {

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

/**
 * Storing image to device gallery
 * @param cr
 * @param source
 * @param title
 * @param description
 * @return
 */
fun insertImage(cr: ContentResolver, source: Bitmap?, title: String?, description: String?): String? {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, title)
    values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
    values.put(MediaStore.Images.Media.DESCRIPTION, description)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    // Add the date meta data to ensure the image is added at the front of the gallery
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    var url: Uri? = null
    var stringUrl: String? = null /* value to be returned */
    try {
        url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (source != null) {
            val imageOut = cr.openOutputStream(url!!)
            try {
                source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut)
            } finally {
                imageOut!!.close()
            }
            val id = ContentUris.parseId(url)
            // Wait until MINI_KIND thumbnail is generated.
            val miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null)
            // This is for backward compatibility.
            storeThumbnail(cr, miniThumb, id, 50f, 50f, MediaStore.Images.Thumbnails.MICRO_KIND)
        } else {
            cr.delete(url!!, null, null)
            url = null
        }
    } catch (e: java.lang.Exception) {
        if (url != null) {
            cr.delete(url, null, null)
            url = null
        }
    }
    if (url != null) {
        stringUrl = url.toString()
    }
    return stringUrl
}

/**
 * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
 * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
 * meta data. The StoreThumbnail method is private so it must be duplicated here.
 *
 * @see MediaStore.Images.Media
 */
private fun storeThumbnail(cr: ContentResolver, source: Bitmap, id: Long, width: Float, height: Float, kind: Int): Bitmap? {

    // create the matrix to scale it
    val matrix = Matrix()
    val scaleX = width / source.width
    val scaleY = height / source.height
    matrix.setScale(scaleX, scaleY)
    val thumb = Bitmap.createBitmap(
        source, 0, 0,
        source.width,
        source.height, matrix,
        true
    )
    val values = ContentValues(4)
    values.put(MediaStore.Images.Thumbnails.KIND, kind)
    values.put(MediaStore.Images.Thumbnails.IMAGE_ID, id.toInt())
    values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.height)
    values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.width)
    val url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values)
    return try {
        val thumbOut = cr.openOutputStream(url!!)
        thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut)
        thumbOut!!.close()
        thumb
    } catch (ex: FileNotFoundException) {
        null
    } catch (ex: IOException) {
        null
    }
}


/**
 *  Encode bitmap into String
 *
 * @param bitmap The bitmap  to encode
 * @return encode form of bitmap
 */
fun convertToString(bitmap: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}


/**
 *  Decode String into Bitmap
 *
 * @param previouslyEncodedImage The String  to convert
 * @return bitmap  of encodedString
 */
fun convertToBitmap(previouslyEncodedImage: String): Bitmap? {
    val bitmap: Bitmap?
    bitmap = if (!previouslyEncodedImage.equals("", ignoreCase = true)) {
        val b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(b, 0, b.size)
    } else {
        null
    }
    return bitmap
}


/**
 *  Processing bitmap
 *
 * @return bitmap  processed bitmap
 */

fun adjustBitmap(bitmap: Bitmap, contrast: Float, brightness: Float, saturation: Float, sharpen: Float): Bitmap {
    val cm = ColorMatrix(floatArrayOf(contrast, 0f, 0f, 0f, brightness, 0f, contrast, 0f, 0f, brightness, 0f, 0f, contrast, 0f, brightness, 0f, 0f, 0f, 1f, 0f))
    val colorSaturation = ColorMatrix()
    colorSaturation.setSaturation(saturation)
    val colorSharpen =
        ColorMatrix(floatArrayOf(1f, 0f, 0f, sharpen, 0f, 0f, 1f, 0f, sharpen / 2, 0f, 0f, 0f, 1f, sharpen / 4, 0f, 0f, 0f, 0f, 1f, 0f))
    cm.postConcat(colorSaturation)
    cm.postConcat(colorSharpen)
    // cm.setSaturation(saturation);
    val ret = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val canvas = Canvas(ret)
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(cm)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return ret
}


/**
 *  Get Uri of Bitmap Image
 *
 * @param bitmap   The bitmap to be converted in uri
 * @return The uri of bitmap
 */
@Suppress("DEPRECATION")
fun Context.getUri(bitmap: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
    return Uri.parse(path)
}

fun captureView(view: View, window: Window, bitmapCallback: (Bitmap) -> Unit) {
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
        // Above Android O, use PixelCopy
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val location = IntArray(2)
        view.getLocationInWindow(location)
        PixelCopy.request(
            window,
            Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
            bitmap,
            {
                if (it == PixelCopy.SUCCESS) {
                    bitmapCallback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    } else {
        val tBitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.RGB_565
        )
        val canvas = Canvas(tBitmap)
        view.draw(canvas)
        canvas.setBitmap(null)
        bitmapCallback.invoke(tBitmap)
    }
}


/**
 * @param sentBitmap
 * @param radius
 * @return blurred image
 */
@Deprecated("")
fun fastblur(sentBitmap: Bitmap, radius: Int): Bitmap? {
    val bitmap = sentBitmap.copy(sentBitmap.config, true)
    if (radius < 1) {
        return null
    }
    val w = bitmap.width
    val h = bitmap.height
    val pix = IntArray(w * h)
    if (bitmap.isRecycled) return null
    bitmap.getPixels(pix, 0, w, 0, 0, w, h)
    val wm = w - 1
    val hm = h - 1
    val wh = w * h
    val div = radius + radius + 1
    val r = IntArray(wh)
    val g = IntArray(wh)
    val b = IntArray(wh)
    var rsum: Int
    var gsum: Int
    var bsum: Int
    var x: Int
    var y: Int
    var i: Int
    var p: Int
    var yp: Int
    var yi: Int
    var yw: Int
    val vmin = IntArray(max(w, h))
    var divsum = div + 1 shr 1
    divsum *= divsum
    val dv = IntArray(256 * divsum)
    i = 0
    while (i < 256 * divsum) {
        dv[i] = i / divsum
        i++
    }
    yi = 0
    yw = yi
    val stack = Array(div) { IntArray(3) }
    var stackpointer: Int
    var stackstart: Int
    var sir: IntArray
    var rbs: Int
    val r1 = radius + 1
    var routsum: Int
    var goutsum: Int
    var boutsum: Int
    var rinsum: Int
    var ginsum: Int
    var binsum: Int
    y = 0
    while (y < h) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        i = -radius
        while (i <= radius) {
            p = pix[yi + min(wm, max(i, 0))]
            sir = stack[i + radius]
            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff
            rbs = r1 - abs(i)
            rsum += sir[0] * rbs
            gsum += sir[1] * rbs
            bsum += sir[2] * rbs
            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }
            i++
        }
        stackpointer = radius
        x = 0
        while (x < w) {
            r[yi] = dv[rsum]
            g[yi] = dv[gsum]
            b[yi] = dv[bsum]
            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum
            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]
            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]
            if (y == 0) {
                vmin[x] = min(x + radius + 1, wm)
            }
            p = pix[yw + vmin[x]]
            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff
            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]
            rsum += rinsum
            gsum += ginsum
            bsum += binsum
            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer % div]
            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]
            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]
            yi++
            x++
        }
        yw += w
        y++
    }
    x = 0
    while (x < w) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        yp = -radius * w
        i = -radius
        while (i <= radius) {
            yi = max(0, yp) + x
            sir = stack[i + radius]
            sir[0] = r[yi]
            sir[1] = g[yi]
            sir[2] = b[yi]
            rbs = r1 - abs(i)
            rsum += r[yi] * rbs
            gsum += g[yi] * rbs
            bsum += b[yi] * rbs
            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }
            if (i < hm) {
                yp += w
            }
            i++
        }
        yi = x
        stackpointer = radius
        y = 0
        while (y < h) {

            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum
            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]
            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]
            if (x == 0) {
                vmin[y] = min(y + r1, hm) * w
            }
            p = x + vmin[y]
            sir[0] = r[p]
            sir[1] = g[p]
            sir[2] = b[p]
            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]
            rsum += rinsum
            gsum += ginsum
            bsum += binsum
            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer]
            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]
            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]
            yi += w
            y++
        }
        x++
    }
    bitmap.setPixels(pix, 0, w, 0, 0, w, h)
    return bitmap
}

fun calculateScaleRatio(imageWidth: Int, imageHeight: Int, viewWidth: Int, viewHeight: Int): Float {
    val ratioWidth = imageWidth.toFloat() / viewWidth
    val ratioHeight = imageHeight.toFloat() / viewHeight
    return max(ratioWidth, ratioHeight)
}

fun calculateThumbnailSize(imageWidth: Int, imageHeight: Int, viewWidth: Int, viewHeight: Int): IntArray? {
    val size = IntArray(2)
    val ratioWidth = imageWidth.toFloat() / viewWidth
    val ratioHeight = imageHeight.toFloat() / viewHeight
    val ratio = max(ratioWidth, ratioHeight)
    if (ratio == ratioWidth) {
        size[0] = viewWidth
        size[1] = (imageHeight / ratio).toInt()
    } else {
        size[0] = (imageWidth / ratio).toInt()
        size[1] = viewHeight
    }
    return size
}

/**
 * Remove all transparent color borders
 *
 * @param bitmap
 * @return
 */
fun cleanImage(bitmap: Bitmap): Bitmap? {
    val width = bitmap.width
    val height = bitmap.height
    var top = 0
    var left = 0
    var right = width
    var bottom = height
    for (idx in 0 until width) if (!isTransparentColumn(bitmap, idx)) {
        left = idx
        break
    }
    for (idx in width - 1 downTo left + 1) if (!isTransparentColumn(bitmap, idx)) {
        right = idx
        break
    }
    for (idx in 0 until height) if (!isTransparentRow(bitmap, idx)) {
        top = idx
        break
    }
    for (idx in height - 1 downTo top + 1) if (!isTransparentRow(bitmap, idx)) {
        bottom = idx
        break
    }
    return Bitmap.createBitmap(bitmap, left, top, right - left + 1, bottom - top + 1)
}

private fun isTransparentRow(bitmap: Bitmap, row: Int): Boolean {
    for (idx in 0 until bitmap.width) if (bitmap.getPixel(idx, row) != Color.TRANSPARENT) {
        return false
    }
    return true
}

private fun isTransparentColumn(bitmap: Bitmap, column: Int): Boolean {
    for (idx in 0 until bitmap.height) if (bitmap.getPixel(column, idx) != Color.TRANSPARENT) {
        return false
    }
    return true
}

@Throws(OutOfMemoryError::class)
fun transparentPadding(image: Bitmap, ratioWidthPerHeight: Float): Bitmap? {
    return try {
        val width = image.width
        val height = image.height
        var x = 0
        var y = 0
        // desfault
        var destWidth = width
        var destHeight = (width / ratioWidthPerHeight).toInt()
        y = (destHeight - height) / 2
        // else
        if (y < 0) {
            destHeight = height
            destWidth = (height * ratioWidthPerHeight).toInt()
            x = max((destWidth - width) / 2, 0)
            y = 0
        }
        val result = Bitmap.createBitmap(destWidth, destHeight, Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawARGB(0x00, 0x00, 0x00, 0x00)
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), Paint())
        result
    } catch (err: OutOfMemoryError) {
        throw err
    }
}

fun createBitmapMask(pointList: ArrayList<PointF>, imageWidth: Int, imageHeight: Int): Bitmap? {
    val bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawARGB(0x00, 0x00, 0x00, 0x00)
    val paint = Paint()
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL_AND_STROKE
    paint.color = Color.BLACK
    val path = Path()
    for (i in 0 until pointList.size) {
        if (i == 0) {
            path.moveTo(pointList[i].x, pointList[i].y)
        } else {
            path.lineTo(pointList[i].x, pointList[i].y)
        }
    }
    canvas.drawPath(path, paint)
    canvas.clipPath(path)
    return bitmap
}

fun cropImage(mainImage: Bitmap, mask: Bitmap, m: Matrix): Bitmap? {
    val canvas = Canvas()
    val result = Bitmap.createBitmap(mainImage.width, mainImage.height, Config.ARGB_8888)
    canvas.setBitmap(result)
    val paint = Paint()
    paint.isFilterBitmap = true
    paint.isAntiAlias = true
    canvas.drawBitmap(mainImage, m, paint)
    paint.xfermode = PorterDuffXfermode(Mode.DST_IN)
    canvas.drawBitmap(mask, 0.toFloat(), 0.toFloat(), paint)
    paint.xfermode = null
    return result
}

fun cropImage(mainImage: Bitmap, mask: Bitmap): Bitmap? {
    val canvas = Canvas()
    val result = Bitmap.createBitmap(mainImage.width, mainImage.height, Config.ARGB_8888)
    canvas.setBitmap(result)
    val paint = Paint()
    paint.isFilterBitmap = true
    paint.isAntiAlias = true
    canvas.drawBitmap(mainImage, 0.toFloat(), 0.toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(Mode.DST_IN)
    canvas.drawBitmap(mask, 0.toFloat(), 0.toFloat(), paint)
    paint.xfermode = null
    return result
}

fun getCircularBitmap(bitmap: Bitmap): Bitmap? {
    val output: Bitmap = if (bitmap.width > bitmap.height) {
        Bitmap.createBitmap(bitmap.height, bitmap.height, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(bitmap.width, bitmap.width, Config.ARGB_8888)
    }
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
    var r = 0f
    r = if (bitmap.width > bitmap.height) {
        (bitmap.height / 2).toFloat()
    } else {
        (bitmap.width / 2).toFloat()
    }
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = Color.BLACK
    canvas.drawCircle(r, r, r, paint)
    paint.xfermode = PorterDuffXfermode(Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)
    return output
}


/**
 * @param src
 * @param type is FLIP_VERTICAL or FLIP_HORIZONTAL
 * @return flipped image
 */
fun flip(src: Bitmap, type: Int): Bitmap? {
    // create new matrix for transformation
    val matrix = Matrix()
    // if vertical
    if (type == FLIP_VERTICAL) {
        // y = y * -1
        matrix.preScale(1.0f, -1.0f)
    } else if (type == FLIP_HORIZONTAL) {
        // x = x * -1
        matrix.preScale(-1.0f, 1.0f)
        // unknown type
    } else {
        return null
    }

    // return transformed image
    return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
}

fun fillBackgroundColorToImage(bitmap: Bitmap, color: Int): Bitmap? {
    val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val canvas = Canvas(result)
    canvas.drawColor(color)
    canvas.drawBitmap(bitmap, 0f, 0f, Paint())
    return result
}

fun rotateImage(src: Bitmap, degs: Float, flip: Boolean): Bitmap? {
    if (degs == 0f) return src
    var bm: Bitmap? = null
    if (src != null) {
        val matrix = Matrix()
        matrix.postRotate(degs)
        if (flip) {
            matrix.postScale(1f, -1f)
        }
        bm = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }
    return bm
}

fun rotateImage(src: Bitmap, degs: Float): Bitmap? {
    return rotateImage(src, degs, false)
}


