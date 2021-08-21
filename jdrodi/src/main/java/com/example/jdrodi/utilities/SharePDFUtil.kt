package com.example.jdrodi.utilities

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import com.example.jdrodi.R
import io.github.inflationx.calligraphy3.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * A Class used to generate PDF for the given Views.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class SharePDFUtil private constructor() {

    companion object {
        private val TAG = SharePDFUtil::class.java.name
        private var sInstance: SharePDFUtil? = null
        val instance: SharePDFUtil?
            get() {
                if (sInstance == null) {
                    sInstance = SharePDFUtil()
                }
                return sInstance
            }
    }

    fun generatePDF(context: Context, filename: String, contentViews: View, listener: PDFUtilListener) {
        contentViews.post {
            val bitmap = contentViews.drawToBitmap()
            GeneratePDFAsync(context, bitmap, filename, listener).execute()
        }


    }

    interface PDFUtilListener {
        fun pdfGenerationSuccess(savedPDFFile: File)
        fun pdfGenerationFailure(exception: Exception)
    }

    /**
     * Async task class used to generate PDF in separate thread.
     */
    inner class GeneratePDFAsync(private val context: Context, var bitmap: Bitmap? = null, private val filename: String, private val listener: PDFUtilListener) :
        AsyncTask<Void?, Void?, File?>() {


        override fun doInBackground(vararg void: Void?): File? {
            return try {

                if (bitmap == null)
                    return null

                val desDirectory = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Environment.getExternalStorageDirectory().path + File.separator + context.getString(R.string.app_name)
                } else {
                    Environment.getExternalStorageDirectory().path + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + context.getString(R.string.app_name)
                }
                val desFile = File(desDirectory)
                if (!desFile.exists()) {
                    desFile.mkdir()
                }
                var outputPath = desDirectory + File.separator + filename + ".pdf"
                if (outputPath.contains("-")) {
                    outputPath = outputPath.replace("-", "_")
                }

                Log.e(TAG, "outputPath: $outputPath")

                // Create PDF Document.
                val pdfDocument = PdfDocument()

                // Write content to PDFDocument.
                writePDFDocument(bitmap!!, pdfDocument)


                // Save document to file.
                savePDFDocumentToStorage(pdfDocument, outputPath)
            } catch (exception: Exception) {
                Log.e(TAG, exception.message!!)
                null
            }
        }

        /**
         * On Post Execute.
         *
         * @param savedPDFFile Saved pdf file, null if not generated successfully
         */
        override fun onPostExecute(savedPDFFile: File?) {
            super.onPostExecute(savedPDFFile)
            if (savedPDFFile != null) {
                //Send Success callback.
                listener.pdfGenerationSuccess(savedPDFFile)
            } else {
                //Send Error callback.
                listener.pdfGenerationFailure(Exception(context.getString(R.string.error_pdf)))
            }
        }

        private fun writePDFDocument(bitmap: Bitmap, pdfDocument: PdfDocument) {
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val pageCanvas = page.canvas
            pageCanvas.drawBitmap(bitmap, Matrix(), Paint())
            pdfDocument.finishPage(page)
        }

        @Throws(IOException::class)
        private fun savePDFDocumentToStorage(pdfDocument: PdfDocument, mFilePath: String): File {
            try {
                val mFile = File(mFilePath)
                if (!mFile.exists()) {
                    mFile.createNewFile()
                }
                Log.i(TAG, "savePDFDocumentToStorage: $mFilePath")
                pdfDocument.writeTo(FileOutputStream(mFile))
                pdfDocument.close()
                return mFile
            } catch (exception: IOException) {
                exception.printStackTrace()
                throw exception
            }
        }

    }


    fun sharePdf(mContext: Context, newFile: File, isWA: Boolean, shareMsg: String) {
        val packageName = "com.whatsapp"

        val contentUri: Uri? = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", newFile)
        if (isWA && mContext.packageManager.getLaunchIntentForPackage(packageName) == null) {
            // if app is not installed
            val msg = mContext.getString(R.string.waapp_not_installed)
            mContext.toast(msg)
            // open play store
            val intent = Intent("android.intent.action.VIEW")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=$packageName")
            mContext.startActivity(intent)
        } else {
            if (contentUri != null) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.type = "application/pdf"
                if (isWA) intent.setPackage(packageName)
                intent.putExtra(Intent.EXTRA_STREAM, contentUri)
                intent.putExtra(Intent.EXTRA_TEXT, shareMsg)
                mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.choose_app)))
            }
        }
    }


}