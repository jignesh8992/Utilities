package com.example.jdrodi.utilities

import android.content.Context
import androidx.annotation.RequiresApi
import android.os.Build
import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.Allocation
import android.renderscript.Element
import android.view.View
import kotlin.math.roundToInt

private const val BITMAP_SCALE = 0.4f
private const val BLUR_RADIUS = 7.5f

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
fun blur(view: View): Bitmap {
    return view.context.blur(getScreenshot(view), BLUR_RADIUS)
}

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.blur(bitmap: Bitmap, f: Float): Bitmap {
    val createScaledBitmap = Bitmap.createScaledBitmap(
        bitmap, (bitmap.width.toFloat() * BITMAP_SCALE).roundToInt(), (bitmap.height.toFloat() * BITMAP_SCALE).roundToInt(), false
    )
    val createBitmap = Bitmap.createBitmap(createScaledBitmap)
    val create = RenderScript.create(this)
    val create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create))
    val createFromBitmap = Allocation.createFromBitmap(create, createScaledBitmap)
    val createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap)
    create2.setRadius(f)
    create2.setInput(createFromBitmap)
    create2.forEach(createFromBitmap2)
    createFromBitmap2.copyTo(createBitmap)
    return createBitmap
}

fun getScreenshot(view: View): Bitmap {
    val createBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    view.draw(Canvas(createBitmap))
    return createBitmap
}