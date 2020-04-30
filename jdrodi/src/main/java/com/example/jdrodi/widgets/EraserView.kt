package com.example.jdrodi.widgets

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Paint.Cap
import android.graphics.Paint.Join
import android.graphics.Shader.TileMode
import android.os.AsyncTask
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatImageView
import com.example.jdrodi.utilities.Log
import com.example.jdrodi.utilities.Toast
import java.util.*
import kotlin.math.abs

private const val ERASE = 1
private const val LASSO = 3
private const val NONE = 0
private const val REDRAW = 4
private const val TARGET = 2

private val TAG = EraserView::class.java.simpleName

@Suppress("DEPRECATION")
class EraserView : AppCompatImageView, OnTouchListener {

    var finalBitmap: Bitmap? = null

    private var mTolerance = 30
    private var mMode = 1
    private var mX = 100.0f
    private var mY = 100.0f
    private var actionListener: ActionListener? = null
    private val brushIndex = ArrayList<Int>()
    private var brushSize = 18
    private var brushSize1 = 18
    private var c2: Canvas? = null
    private val changesIndex = ArrayList<Path>()
    private var mContext: Context? = null
    private var curIndex = -1
    private var drawLasso = false
    private var drawOnLasso = true
    private var drawPath = Path()
    private var erPaint = Paint()
    private var erPaint1 = Paint()
    private var erps = dpToPx(context, 2)
    private var insideCutEnable = true
    private var isNewPath = false
    private var isTouchEnable = true

    private var isTouched = false
    private var lPath = Path()
    private val lassoIndex = ArrayList<Boolean>()
    private val modeIndex = ArrayList<Int>()
    private var offset = 200
    private var offset1 = 200
    private var orgBit: Bitmap? = null
    private var paint = Paint()
    private var mPc = 0
    private var progressDialog: ProgressDialog? = null
    private var point: Point? = null
    private var sX = 0f
    private var sY = 0f
    private var scale = 1.0f
    private var tPath = Path()
    private var targetBrushSize = 18
    private var targetBrushSize1 = 18
    private var undoRedoListener: UndoRedoListener? = null
    private var updateOnly = false
    private val vectorPoints = ArrayList<Vector<Point>?>()


    constructor(context: Context) : super(context) {
        initPaint(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaint(context)
    }


    interface ActionListener {
        fun onActionCompleted(i: Int)
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskRunner internal constructor(var ch: Int) : AsyncTask<Void?, Void?, Bitmap?>() {

        var targetPoints: Vector<Point>? = null

        override fun doInBackground(vararg params: Void?): Bitmap? {
            if (ch != 0) {
                targetPoints = Vector()
                floodFill(finalBitmap, point, ch)
                for (k in targetPoints!!.indices) {
                    val mPoint = targetPoints!![k]
                    finalBitmap!!.setPixel(mPoint.x, mPoint.y, 0)
                }
                changesIndex.add(curIndex + 1, Path())
                brushIndex.add(curIndex + 1, brushSize)
                modeIndex.add(curIndex + 1, TARGET)
                vectorPoints.add(curIndex + 1, Vector(targetPoints!!))
                lassoIndex.add(curIndex + 1, insideCutEnable)
                curIndex += 1
                clearNextChanges()
                updateOnly = true
                Log.i(TAG, "Time : " + ch + "  " + curIndex + "   " + changesIndex.size)
            }
            return null
        }

        private fun floodFill(bmp: Bitmap?, pt: Point?, targetColor: Int) {
            if (targetColor != 0) {
                val q: Queue<Point?> = LinkedList()
                q.add(pt)
                while (q.size > 0) {
                    val pointN = q.poll()!!
                    if (compareColor(bmp!!.getPixel(pointN.x, pointN.y), targetColor)) {
                        val e = Point(pointN.x + 1, pointN.y)
                        while (pointN.x > 0 && compareColor(bmp.getPixel(pointN.x, pointN.y), targetColor)) {
                            bmp.setPixel(pointN.x, pointN.y, 0)
                            targetPoints!!.add(Point(pointN.x, pointN.y))
                            if (pointN.y > 0 && compareColor(bmp.getPixel(pointN.x, pointN.y - 1), targetColor)) {
                                q.add(Point(pointN.x, pointN.y - 1))
                            }
                            if (pointN.y < bmp.height - 1 && compareColor(bmp.getPixel(pointN.x, pointN.y + 1), targetColor)) {
                                q.add(Point(pointN.x, pointN.y + 1))
                            }
                            pointN.x--
                        }
                        while (e.x < bmp.width - 1 && compareColor(bmp.getPixel(e.x, e.y), targetColor)) {
                            bmp.setPixel(e.x, e.y, 0)
                            targetPoints!!.add(Point(e.x, e.y))
                            if (e.y > 0 && compareColor(bmp.getPixel(e.x, e.y - 1), targetColor)) {
                                q.add(Point(e.x, e.y - 1))
                            }
                            if (e.y < bmp.height - 1 && compareColor(bmp.getPixel(e.x, e.y + 1), targetColor)) {
                                q.add(Point(e.x, e.y + 1))
                            }
                            e.x++
                        }
                    }
                }
            }
        }

        private fun compareColor(color1: Int, color2: Int): Boolean {
            if (color1 == 0 || color2 == 0) {
                return false
            }
            if (color1 == color2) {
                return true
            }
            val diffRed = abs(Color.red(color1) - Color.red(color2))
            val diffGreen = abs(Color.green(color1) - Color.green(color2))
            val diffBlue = abs(Color.blue(color1) - Color.blue(color2))
            return diffRed <= mTolerance && diffGreen <= mTolerance && diffBlue <= mTolerance
        }

        private fun clearNextChanges() {
            var size = changesIndex.size
            Log.i(TAG, " Curindex $curIndex Size $size")
            val i = curIndex + 1
            while (size > i) {
                Log.i(TAG, " index $i")
                changesIndex.removeAt(i)
                brushIndex.removeAt(i)
                modeIndex.removeAt(i)
                vectorPoints.removeAt(i)
                lassoIndex.removeAt(i)
                size = changesIndex.size
            }
            if (undoRedoListener != null) {
                undoRedoListener!!.enableUndo(true)
                undoRedoListener!!.enableRedo(false)
            }
            if (actionListener != null) {
                actionListener!!.onActionCompleted(mMode)
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Processing...")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        override fun onPostExecute(v: Bitmap?) {
            progressDialog!!.dismiss()
            invalidate()
        }


    }


    interface UndoRedoListener {
        fun enableRedo(z: Boolean)
        fun enableUndo(z: Boolean)
    }


    private fun initPaint(context: Context) {
        mContext = context
        isFocusable = true
        isFocusableInTouchMode = true
        brushSize = dpToPx(getContext(), brushSize)
        brushSize1 = dpToPx(getContext(), brushSize)
        targetBrushSize = dpToPx(getContext(), 50)
        targetBrushSize1 = dpToPx(getContext(), 50)
        paint.alpha = 0
        paint.color = 0
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Join.ROUND
        paint.strokeCap = Cap.ROUND
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        paint.isAntiAlias = true
        paint.strokeWidth = updatebrushsize(brushSize1, scale)
        erPaint = Paint()
        erPaint.isAntiAlias = true
        erPaint.color = -65536
        erPaint.isAntiAlias = true
        erPaint.style = Paint.Style.STROKE
        erPaint.strokeJoin = Join.MITER
        erPaint.strokeWidth = updatebrushsize(erps, scale)
        erPaint1 = Paint()
        erPaint1.isAntiAlias = true
        erPaint1.color = -65536
        erPaint1.isAntiAlias = true
        erPaint1.style = Paint.Style.STROKE
        erPaint1.strokeJoin = Join.MITER
        erPaint1.strokeWidth = updatebrushsize(erps, scale)
        erPaint1.pathEffect = DashPathEffect(floatArrayOf(10.0f, 20.0f), 0.0f)
    }

    override fun setImageBitmap(bm: Bitmap) {
        orgBit = bm.copy(Bitmap.Config.ARGB_8888, true)
        finalBitmap = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)
        c2 = Canvas()
        c2!!.setBitmap(finalBitmap)
        c2!!.drawBitmap(bm, 0.0f, 0.0f, null)
        enableTouchClear(isTouchEnable)
        super.setImageBitmap(finalBitmap)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (c2 != null) {
            val p = Paint()
            if (!updateOnly) {
                if (isTouched) {
                    paint = getPaintByMode(mMode, brushSize)
                    c2!!.drawPath(tPath, paint)
                    isTouched = false
                } else if (curIndex >= 0 && drawOnLasso) {
                    redrawCanvas()
                }
            }

            if (mMode == TARGET) {
                p.color = -65536
                erPaint.strokeWidth = updatebrushsize(erps, scale)
                canvas.drawCircle(mX, mY, (targetBrushSize / 2).toFloat(), erPaint)
                canvas.drawCircle(mX, mY + offset.toFloat(), updatebrushsize(dpToPx(context, 7), scale), p)
                p.strokeWidth = updatebrushsize(dpToPx(context, 1), scale)
                canvas.drawLine(mX - (targetBrushSize / 2).toFloat(), mY, (targetBrushSize / 2).toFloat() + mX, mY, p)
                canvas.drawLine(mX, mY - (targetBrushSize / 2).toFloat(), mX, (targetBrushSize / 2).toFloat() + mY, p)
                drawOnLasso = true
            }
            if (mMode == LASSO) {
                p.color = -65536
                erPaint.strokeWidth = updatebrushsize(erps, scale)
                canvas.drawCircle(mX, mY, (targetBrushSize / 2).toFloat(), erPaint)
                canvas.drawCircle(mX, mY + offset.toFloat(), updatebrushsize(dpToPx(context, 7), scale), p)
                p.strokeWidth = updatebrushsize(dpToPx(context, 1), scale)
                canvas.drawLine(mX - (targetBrushSize / 2).toFloat(), mY, (targetBrushSize / 2).toFloat() + mX, mY, p)
                canvas.drawLine(mX, mY - (targetBrushSize / 2).toFloat(), mX, (targetBrushSize / 2).toFloat() + mY, p)
                if (!drawOnLasso) {
                    erPaint1.strokeWidth = updatebrushsize(erps, scale)
                    canvas.drawPath(lPath, erPaint1)
                }
            }
            if (mMode == ERASE || mMode == REDRAW) {
                p.color = -65536
                erPaint.strokeWidth = updatebrushsize(erps, scale)
                canvas.drawCircle(mX, mY, (brushSize / 2).toFloat(), erPaint)
                canvas.drawCircle(mX, mY + offset.toFloat(), updatebrushsize(dpToPx(context, 7), scale), p)
            }
            updateOnly = false
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (mMode == TARGET) {
            drawOnLasso = false
            mX = event.x
            mY = event.y - offset.toFloat()
            when (event.action) {
                0, 2 -> invalidate()
                1 -> {
                    if (mX >= 0.0f && mY >= 0.0f && mX <= finalBitmap!!.width.toFloat() && mY <= finalBitmap!!.height.toFloat()) {
                        point = Point(mX.toInt(), mY.toInt())
                        mPc = finalBitmap!!.getPixel(mX.toInt(), mY.toInt())
                        AsyncTaskRunner(mPc).execute()
                    }
                    invalidate()
                }
            }
        }
        if (mMode == LASSO) {
            mX = event.x
            mY = event.y - offset.toFloat()
            when (event.action) {
                0 -> {
                    isNewPath = true
                    drawOnLasso = false
                    sX = mX
                    sY = mY
                    lPath = Path()
                    lPath.moveTo(mX, mY)
                    invalidate()
                }
                1 -> {
                    lPath.lineTo(mX, mY)
                    lPath.lineTo(sX, sY)
                    drawLasso = true
                    invalidate()
                    if (actionListener != null) {
                        actionListener!!.onActionCompleted(5)
                    }
                }
                2 -> {
                    lPath.lineTo(mX, mY)
                    invalidate()
                }
                else -> return false
            }
        }
        if (mMode == ERASE || mMode == REDRAW) {
            mX = event.x
            mY = event.y - offset.toFloat()
            isTouched = true
            when (event.action) {
                0 -> {
                    paint.strokeWidth = brushSize.toFloat()
                    tPath = Path()
                    tPath.moveTo(mX, mY)
                    drawPath.moveTo(mX, mY)
                    invalidate()
                }
                1 -> {
                    drawPath.lineTo(mX, mY)
                    tPath.lineTo(mX, mY)
                    invalidate()
                    changesIndex.add(curIndex + 1, Path(tPath))
                    brushIndex.add(curIndex + 1, brushSize)
                    modeIndex.add(curIndex + 1, mMode)
                    vectorPoints.add(curIndex + 1, null)
                    lassoIndex.add(curIndex + 1, insideCutEnable)
                    tPath.reset()
                    curIndex++
                    clearNextChanges()
                }
                2 -> {
                    drawPath.lineTo(mX, mY)
                    tPath.lineTo(mX, mY)
                    invalidate()
                }
                else -> return false
            }
        }
        return true
    }

    private fun clearNextChanges() {
        var size = changesIndex.size
        Log.i(TAG, "ClearNextChange Current index $curIndex Size $size")
        val i = curIndex + 1
        while (size > i) {
            Log.i(TAG, " index $i")
            changesIndex.removeAt(i)
            brushIndex.removeAt(i)
            modeIndex.removeAt(i)
            vectorPoints.removeAt(i)
            lassoIndex.removeAt(i)
            size = changesIndex.size
        }
        if (undoRedoListener != null) {
            undoRedoListener!!.enableUndo(true)
            undoRedoListener!!.enableRedo(false)
        }
        if (actionListener != null) {
            actionListener!!.onActionCompleted(mMode)
        }
    }

    fun setMODE(m: Int) {
        mMode = m
    }

    private fun getPaintByMode(mode: Int, brushSize: Int): Paint {
        paint = Paint()
        paint.alpha = 0
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Join.ROUND
        paint.strokeCap = Cap.ROUND
        paint.isAntiAlias = true
        paint.strokeWidth = brushSize.toFloat()
        if (mode == ERASE) {
            paint.color = 0
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
        if (mode == REDRAW) {
            paint.color = -1
            paint.shader = BitmapShader(orgBit!!, TileMode.REPEAT, TileMode.REPEAT)
        }
        return paint
    }

    fun setOffset(os: Int) {
        offset1 = os
        offset = updatebrushsize(os, scale).toInt()
        updateOnly = true
    }

    fun getOffset(): Int {
        return offset1
    }

    fun setRadius(r: Int) {
        brushSize1 = dpToPx(context, r)
        brushSize = updatebrushsize(brushSize1, scale).toInt()
        updateOnly = true
    }

    private fun updatebrushsize(currentbs: Int, scale: Float): Float {
        return currentbs.toFloat() / scale
    }

    fun updateOnScale(scale: Float) {
        Log.i(TAG, "Scale $scale  Brush size  $brushSize")
        brushSize = updatebrushsize(brushSize1, scale).toInt()
        targetBrushSize = updatebrushsize(targetBrushSize1, scale).toInt()
        offset = updatebrushsize(offset1, scale).toInt()
    }

    fun setThreshold(th: Int) {
        mTolerance = th
        if (curIndex >= 0) {
            Log.i(TAG, " Threshold " + th + "  " + (modeIndex[curIndex] == TARGET))
        }
    }

    fun enableTouchClear(b: Boolean) {
        isTouchEnable = b
        if (b) {
            setOnTouchListener(this)
        } else {
            setOnTouchListener(null)
        }
    }

    @SuppressLint("WrongConstant")
    fun enableInsideCut(enable: Boolean) {
        insideCutEnable = enable
        if (isNewPath && drawLasso) {
            Log.i(TAG, " draw lassso   on up ")
            drawLassoPath(lPath, insideCutEnable)
            changesIndex.add(curIndex + 1, Path(lPath))
            brushIndex.add(curIndex + 1, brushSize)
            modeIndex.add(curIndex + 1, mMode)
            vectorPoints.add(curIndex + 1, null)
            lassoIndex.add(curIndex + 1, insideCutEnable)
            lPath.reset()
            curIndex++
            clearNextChanges()
            drawOnLasso = false
            invalidate()
            return
        }
        Toast.short(mContext!!, "Please Draw a closed path!!!")
    }

    fun undoChange() {
        setImageBitmap(orgBit!!)
        Log.i(TAG, "Performing UNDO Curindex " + curIndex + "  " + changesIndex.size)
        if (curIndex >= 0) {
            curIndex--
            redrawCanvas()
            Log.i(TAG, " Curindex " + curIndex + "  " + changesIndex.size)
            if (curIndex < 0 && undoRedoListener != null) {
                undoRedoListener!!.enableUndo(false)
            }
            if (undoRedoListener != null) {
                undoRedoListener!!.enableRedo(true)
            }
        }
    }

    fun redoChange() {
        Log.i(TAG, (curIndex + 1 >= changesIndex.size).toString() + " Curindex " + curIndex + " " + changesIndex.size)
        if (curIndex + 1 < changesIndex.size) {
            setImageBitmap(orgBit!!)
            curIndex++
            redrawCanvas()
            if (curIndex + 1 >= changesIndex.size && undoRedoListener != null) {
                undoRedoListener!!.enableRedo(false)
            }
            if (undoRedoListener != null) {
                undoRedoListener!!.enableUndo(true)
            }
        }
    }

    private fun redrawCanvas() {
        var i = 0
        while (i <= curIndex) {
            if (modeIndex[i] == ERASE || modeIndex[i] == REDRAW) {
                tPath = Path(changesIndex[i])
                paint = getPaintByMode(modeIndex[i], brushIndex[i])
                c2!!.drawPath(tPath, paint)
                tPath.reset()
            }
            if (modeIndex[i] == TARGET) {
                val mVectorPoint = vectorPoints[i]
                for (k in mVectorPoint!!.indices) {
                    val mPoint = mVectorPoint[k]
                    finalBitmap!!.setPixel(mPoint.x, mPoint.y, 0)
                }
            }
            if (modeIndex[i] == LASSO) {
                Log.i(TAG, " onDraw Lassoo ")
                drawLassoPath(Path(changesIndex[i]), lassoIndex[i])
            }
            i++
        }
    }

    private fun drawLassoPath(path: Path, insideCut: Boolean) {
        val paint: Paint
        if (insideCut) {
            paint = Paint()
            paint.isAntiAlias = true
            paint.color = 0
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            c2!!.drawPath(path, paint)
        } else {
            val resultingImage = finalBitmap!!.copy(finalBitmap!!.config, true)
            Canvas(resultingImage).drawBitmap(finalBitmap!!, 0.0f, 0.0f, null)
            c2!!.drawColor(NONE, PorterDuff.Mode.CLEAR)
            paint = Paint()
            paint.isAntiAlias = true
            c2!!.drawPath(path, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            c2!!.drawBitmap(resultingImage, 0.0f, 0.0f, paint)
        }
        drawLasso = false
        drawOnLasso = true
        isNewPath = false
    }

    companion object {
        fun dpToPx(c: Context, dp: Int): Int {
            val f = dp.toFloat()
            c.resources
            return (Resources.getSystem().displayMetrics.density * f).toInt()
        }
    }
}