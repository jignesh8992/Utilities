package com.example.jdrodi.utilities

import kotlin.Throws
import android.annotation.SuppressLint
import android.view.WindowManager
import android.provider.MediaStore
import android.os.Build
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

fun getnewHeight(i: Int, i2: Int, f: Float, f2: Float): Float {
    return i2.toFloat() * f / i.toFloat()
}

fun getnewWidth(i: Int, i2: Int, f: Float, f2: Float): Float {
    return i.toFloat() * f2 / i2.toFloat()
}

@Throws(IOException::class)
fun Context.getResampleImageBitmap(uri: Uri, context: Context): Bitmap {
    val realPathFromURI = getRealPathFromURI(uri)
    return try {
        @SuppressLint("WrongConstant") val defaultDisplay = (context.getSystemService("window") as WindowManager).defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        var i = point.x
        val dpToPx = point.y - dpToPx(context, 110)
        if (i <= dpToPx) {
            i = dpToPx
        }
        resampleImage(realPathFromURI!!, i)
    } catch (e: Exception) {
        e.printStackTrace()
        MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(realPathFromURI))
    }
}

@Throws(IOException::class)
fun Context.getResampleImageBitmap(uri: Uri, i: Int): Bitmap? {
    return try {
        resampleImage(getRealPathFromURI(uri)!!, i)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Throws(Exception::class)
fun resampleImage(str: String, i: Int): Bitmap {
    var exifRotation: Int = 0
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(str, options)
    val options2 = BitmapFactory.Options()
    options2.inSampleSize = getClosestResampleSize(options.outWidth, options.outHeight, i)
    val decodeFile = BitmapFactory.decodeFile(str, options2)
    val matrix = Matrix()
    if (decodeFile.width > i || decodeFile.height > i) {
        val resampling = getResampling(decodeFile.width, decodeFile.height, i)
        matrix.postScale(resampling.outWidth.toFloat() / decodeFile.width.toFloat(), resampling.outHeight.toFloat() / decodeFile.height.toFloat())
    }
    if (Build.VERSION.SDK.toInt() > 4 && getExifRotation(str).also { exifRotation = it } != 0) {
        matrix.postRotate(exifRotation.toFloat())
    }
    return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.width, decodeFile.height, matrix, true)
}

fun getResampling(i: Int, i2: Int, i3: Int): BitmapFactory.Options {
    val f: Float
    val f2: Float
    val options = BitmapFactory.Options()
    if (i <= i2 && i2 > i) {
        f = i3.toFloat()
        f2 = i2.toFloat()
    } else {
        f = i3.toFloat()
        f2 = i.toFloat()
    }
    val f3 = f / f2
    options.outWidth = (i.toFloat() * f3 + 0.5f).toInt()
    options.outHeight = (i2.toFloat() * f3 + 0.5f).toInt()
    return options
}

fun getClosestResampleSize(i: Int, i2: Int, i3: Int): Int {
    val max = Math.max(i, i2)
    var i4 = 1
    while (true) {
        if (i4 >= Int.MAX_VALUE) {
            break
        } else if (i4 * i3 > max) {
            i4--
            break
        } else {
            i4++
        }
    }
    return if (i4 > 0) {
        i4
    } else 1
}

@Throws(Exception::class)
fun getBitmapDims(str: String): BitmapFactory.Options {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(str, options)
    return options
}

fun Context.getRealPathFromURI(uri: Uri): String? {
    return try {
        val query = contentResolver.query(uri, null, null, null, null) ?: return uri.path
        query.moveToFirst()
        val string = query.getString(query.getColumnIndex("_data"))
        query.close()
        string
    } catch (e: Exception) {
        e.printStackTrace()
        uri.toString()
    }
}

fun dpToPx(context: Context, i: Int): Int {
    context.resources
    return (i.toFloat() * Resources.getSystem().displayMetrics.density).toInt()
}

fun pxToDp(context: Context, f: Float): Float {
    return f / (context.resources.displayMetrics.densityDpi.toFloat() / 160.0f)
}

fun bitmapmasking(bitmap: Bitmap, bitmap2: Bitmap?): Bitmap {
    val createBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(createBitmap)
    val paint = Paint(1)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    canvas.drawBitmap(bitmap, 0.0f, 0.0f, null as Paint?)
    canvas.drawBitmap(bitmap2!!, 0.0f, 0.0f, paint)
    paint.xfermode = null
    return createBitmap
}

fun getThumbnail(bitmap: Bitmap, i: Int, i2: Int): Bitmap {
    val bitmap2: Bitmap?
    val copy = bitmap.copy(bitmap.config, true)
    bitmap.recycle()
    val width = copy.width
    val height = copy.height
    bitmap2 = if (height > width) {
        cropCenterBitmap(copy, width, width)
    } else {
        cropCenterBitmap(copy, height, height)
    }
    return Bitmap.createScaledBitmap(bitmap2!!, i, i2, true)
}

fun cropCenterBitmap(bitmap: Bitmap?, i: Int, i2: Int): Bitmap? {
    var i = i
    var i2 = i2
    if (bitmap == null) {
        return null
    }
    val width = bitmap.width
    val height = bitmap.height
    if (width < i && height < i2) {
        return bitmap
    }
    var i3 = 0
    val i4 = if (width > i) (width - i) / 2 else 0
    if (height > i2) {
        i3 = (height - i2) / 2
    }
    if (i > width) {
        i = width
    }
    if (i2 > height) {
        i2 = height
    }
    return Bitmap.createBitmap(bitmap, i4, i3, i, i2)
}

fun cropCenterBitmap(bitmap: Bitmap?): Bitmap? {
    if (bitmap == null) {
        return null
    }
    var width = bitmap.width
    var height = bitmap.height
    val i = if (width > height) height else width
    if (width < i && height < i) {
        return bitmap
    }
    var i2 = 0
    val i3 = if (width > i) (width - i) / 2 else 0
    if (height > i) {
        i2 = (height - i) / 2
    }
    if (i <= width) {
        width = i
    }
    if (i <= height) {
        height = i
    }
    return Bitmap.createBitmap(bitmap, i3, i2, width, height)
}

fun mergelogo(bitmap: Bitmap, bitmap2: Bitmap): Bitmap {
    var bitmap2 = bitmap2
    val createBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val width = bitmap.width.toFloat()
    val height = bitmap.height.toFloat()
    val width2 = bitmap2.width.toFloat()
    val height2 = bitmap2.height.toFloat()
    val f = width2 / height2
    val f2 = height2 / width2
    if (width2 > width) {
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, width.toInt(), (f2 * width) as Int, false)
    } else if (height2 > height) {
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, (f * height).toInt(), height.toInt(), false)
    }
    val canvas = Canvas(createBitmap)
    canvas.drawBitmap(bitmap, 0.0f, 0.0f, null as Paint?)
    canvas.drawBitmap(bitmap2, (bitmap.width - bitmap2.width).toFloat(), (bitmap.height - bitmap2.height).toFloat(), null as Paint?)
    return createBitmap
}

fun CropBitmapTransparency(bitmap: Bitmap): Bitmap? {
    var width = bitmap.width
    var height = bitmap.height
    var i = -1
    var i2 = -1
    for (i3 in 0 until bitmap.height) {
        for (i4 in 0 until bitmap.width) {
            if (bitmap.getPixel(i4, i3) shr 24 and 255 > 0) {
                if (i4 < width) {
                    width = i4
                }
                if (i4 > i) {
                    i = i4
                }
                if (i3 < height) {
                    height = i3
                }
                if (i3 > i2) {
                    i2 = i3
                }
            }
        }
    }
    return if (i < width || i2 < height) {
        null
    } else Bitmap.createBitmap(bitmap, width, height, i - width + 1, i2 - height + 1)
}

fun getTiledBitmap(context: Context, i: Int, i2: Int, i3: Int): Bitmap {
    val rect = Rect(0, 0, i2, i3)
    val paint = Paint()
    val options = BitmapFactory.Options()
    options.inScaled = false
    paint.shader = BitmapShader(BitmapFactory.decodeResource(context.resources, i, options), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    val createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888)
    Canvas(createBitmap).drawRect(rect, paint)
    return createBitmap
}

fun getColoredBitmap(i: Int, i2: Int, i3: Int): Bitmap {
    val createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888)
    Canvas(createBitmap).drawColor(i)
    return createBitmap
}

fun getRotatedBitmap(bitmap: Bitmap, f: Float): Bitmap {
    val createBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(createBitmap)
    val matrix = Matrix()
    matrix.postTranslate((-bitmap.width / 2).toFloat(), (-bitmap.height / 2).toFloat())
    matrix.postRotate(f)
    matrix.postTranslate((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat())
    canvas.drawBitmap(bitmap, matrix, null)
    return createBitmap
}

@Throws(Exception::class)
fun resampleImageAndSaveToNewLocation(str: String, str2: String) {
    resampleImage(str, 800).compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(str2))
}


fun cropBitmapTransparency(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val iArr = IntArray(width * height)
    bitmap.getPixels(iArr, 0, width, 0, 0, width, height)
    var i = -1
    var i2 = width
    var i3 = height
    var i4 = -1
    for (i5 in 0 until height) {
        for (i6 in 0 until width) {
            if (iArr[i5 * width + i6] == 0) {
                if (i6 < i2) {
                    i2 = i6
                }
                if (i6 > i) {
                    i = i6
                }
                if (i5 < i3) {
                    i3 = i5
                }
                if (i5 > i4) {
                    i4 = i5
                }
            }
        }
    }
    return if (i < i2 || i4 < i3) bitmap else Bitmap.createBitmap(bitmap, i2, i3, i - i2 + 1, i4 - i3 + 1)
}

fun Activity.saveBitmapObject(bitmap: Bitmap): String? {
    return try {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Story Maker/.data")
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.d("", "Can't create directory to save image.")
                return null
            }
        }
        val str = file.path + File.separator + ("raw1_" + System.currentTimeMillis() + ".png")
        val file2 = File(str)
        try {
            if (!file2.exists()) {
                file2.createNewFile()
            }
            val fileOutputStream = FileOutputStream(file2)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            sendBroadcast(Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        str
    } catch (e2: Exception) {
        e2.printStackTrace()
        null
    }
}

fun cropInRatio(bitmap: Bitmap, i: Int, i2: Int): Bitmap {
    val width = bitmap.width.toFloat()
    val height = bitmap.height.toFloat()
    val f = getnewHeight(i, i2, width, height)
    val f2 = getnewWidth(i, i2, width, height)
    var createBitmap = if (f2 > width || f2 >= width) null else Bitmap.createBitmap(bitmap, ((width - f2) / 2.0f).toInt(), 0, f2.toInt(), height.toInt())
    if (f <= height && f < height) {
        createBitmap = Bitmap.createBitmap(bitmap, 0, ((height - f) / 2.0f).toInt(), width.toInt(), f.toInt())
    }
    return if (f2 == width && f == height) bitmap else createBitmap!!
}