package com.example.jdrodi.utilities

import android.content.*
import android.content.res.Resources
import android.graphics.*
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
import com.example.jdrodi.R
import com.example.jdrodi.utilities.FileHelper.createTempUri
import java.io.*

private val TAG = BitmapHelper::class.java.simpleName

@Suppress("unused")
object BitmapHelper {

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
     * ToDO.. Return bitmap of view
     *
     * @param view The view to be converted into bitmap
     * @return The bitmap of view
     */
    @Suppress("DEPRECATION")
    fun getBitmap(view: View): Bitmap? {
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


}
