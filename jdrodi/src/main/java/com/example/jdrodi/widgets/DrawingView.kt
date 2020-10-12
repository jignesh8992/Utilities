package com.example.jdrodi.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.jdrodi.utilities.getBitmap
import java.util.*

class DrawingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {
    // paint
    private val mPaintSrcIn = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val mPaintDstIn = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val mPaintColor = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintEraser = Paint(Paint.ANTI_ALIAS_FLAG)

    // canvas
    private val mLayerCanvas = Canvas()
    private var mLayerBitmap: Bitmap? = null

    // draw lines
    private var isLine = false
    private val mDrawLines = ArrayList<DrawLine>()
    private val mUndoneOps = ArrayList<DrawLine>()
    private val mCurrentLine = DrawLine()

    // draw stickers
    private var isStickers = false
    private var mCurrentSticker = DrawStickers()
    private val mTouches: ArrayList<DrawStickers>
    private val mUndoTouches: ArrayList<DrawStickers>
    private var mSticker: Bitmap?


    init {
        mPaintSrcIn.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        mPaintDstIn.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        mPaintColor.style = Paint.Style.STROKE
        mPaintColor.strokeJoin = Paint.Join.ROUND
        mPaintColor.strokeCap = Paint.Cap.ROUND
        mPaintEraser.set(mPaintColor)
        mPaintEraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mPaintEraser.maskFilter = BlurMaskFilter(
            resources.displayMetrics.density * 4,
            BlurMaskFilter.Blur.NORMAL
        )
        mTouches = ArrayList()
        mUndoTouches = ArrayList()
        val bitmap = getBitmap(context, "spray/1.png")!!
        mSticker = bitmap
        val point = Point()
        point.x = 0
        point.y = 0
        mCurrentSticker = DrawStickers(point, mSticker)
    }

    fun setColor(color: Int) {
        mCurrentLine.reset()
        mCurrentLine.type = DrawLine.Type.PAINT
        mCurrentLine.color = color
    }

    fun setStroke(stroke: Int) {
        mCurrentLine.reset()
        mCurrentLine.type = DrawLine.Type.PAINT
        mCurrentLine.stroke = stroke
    }

    fun setSticker(mSticker: Bitmap?) {
        this.mSticker = mSticker
    }

    fun setLine(isLine: Boolean) {
        this.isLine = isLine
    }

    fun setSticker(isStickers: Boolean) {
        this.isStickers = isStickers
    }

    fun undoLine() {
        if (mDrawLines.size > 0) {
            val last =
                mDrawLines.removeAt(mDrawLines.size - 1)
            mUndoneOps.add(last)
            invalidate()
        }
    }

    fun undoSticker() {
        if (mTouches.size > 0) {
            val last = mTouches.removeAt(mTouches.size - 1)
            mUndoTouches.add(last)
            invalidate()
        }
    }

    fun redoLine() {
        if (mUndoneOps.size > 0) {
            val redo =
                mUndoneOps.removeAt(mUndoneOps.size - 1)
            mDrawLines.add(redo)
            invalidate()
        }
    }

    fun redoSticker() {
        if (mUndoTouches.size > 0) {
            val redo = mUndoTouches.removeAt(mUndoTouches.size - 1)
            mTouches.add(redo)
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mLayerBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mLayerCanvas.setBitmap(mLayerBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            return
        }
        // Clear software canvas
        mLayerCanvas.drawColor(0, PorterDuff.Mode.CLEAR)
        // Draw picture from ops
        for (op in mDrawLines) {
            drawOp(mLayerCanvas, op)
        }
        drawOp(mLayerCanvas, mCurrentLine)
        canvas.drawBitmap(mLayerBitmap!!, 0f, 0f, null)
        for (stickers in mTouches) {
            canvas.drawBitmap(
                stickers.bitmap!!,
                stickers.point.x.toFloat(),
                stickers.point.y.toFloat(),
                null
            )
            invalidate()
        }
    }

    private fun drawOp(
        canvas: Canvas,
        op: DrawLine
    ) {
        if (op.path.isEmpty) {
            return
        }
        val paint: Paint
        if (op.type == DrawLine.Type.PAINT) {
            paint = mPaintColor
            paint.color = op.color
            paint.strokeWidth = op.stroke.toFloat()
        } else {
            paint = mPaintEraser
            paint.strokeWidth = op.stroke.toFloat()
        }
        mLayerCanvas.drawPath(op.path, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        if (isLine || isStickers) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (isLine) {
                    mUndoneOps.clear()
                    mCurrentLine.path.moveTo(x, y)
                } else {
                    mUndoTouches.clear()
                    mCurrentSticker.point.x = x.toInt() - 100
                    mCurrentSticker.point.y = y.toInt() - 100
                    isStickers = false
                    Handler().postDelayed({ isStickers = true }, 50)
                }
                MotionEvent.ACTION_MOVE -> if (isLine) {
                    var i = 0
                    while (i < event.historySize) {
                        mCurrentLine.path.lineTo(
                            event.getHistoricalX(i), event
                                .getHistoricalY(i)
                        )
                        i++
                    }
                    mCurrentLine.path.lineTo(x, y)
                } else {
                    val point = Point()
                    point.x = x.toInt() - 100
                    point.y = y.toInt() - 100
                    mTouches.add(DrawStickers(point, mSticker))
                    isStickers = false
                    Handler().postDelayed({ isStickers = true }, 50)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (isLine) {
                    mCurrentLine.path.lineTo(x, y)
                    mDrawLines.add(DrawLine(mCurrentLine))
                    mCurrentLine.path.reset()
                } else {
                    val point = Point()
                    point.x = x.toInt() - 100
                    point.y = y.toInt() - 100
                    mTouches.add(DrawStickers(point, mSticker))
                }
            }
            invalidate()
        } else {
            // removeBorder();
        }
        return true
    }

    private class DrawLine {
        val path = Path()
        var type: Type? = null
        var color = 0
        var stroke = 0

        constructor() {
            //
        }

        constructor(op: DrawLine) {
            path.set(op.path)
            type = op.type
            color = op.color
            stroke = op.stroke
        }

        fun reset() {
            path.reset()
        }

        enum class Type {
            PAINT, ERASE
        }
    }

    private class DrawStickers {
        var point = Point()
        var bitmap: Bitmap? = null

        constructor() {}
        constructor(point: Point, bitmap: Bitmap?) {
            this.point = point
            this.bitmap = bitmap
        }
    }


}