package com.example.jdrodi.utilities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.jdrodi.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


private val TAG = IntentActions::class.java.simpleName

object IntentActions {

    @SuppressLint("MissingPermission")
    fun makeCall(mContext: Context, pNumber: String) {
        try {
            val intent = Intent(ACTION_CALL)
            intent.data = Uri.parse("tel:$pNumber")
            mContext.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun openWhatsApp(mContext: Context, pNumber: String) {
        try {
            val textMsg = ""
            val sb = StringBuilder()
            sb.append("whatsapp://send/?text=")
            sb.append(URLEncoder.encode(textMsg, "UTF-8"))
            sb.append("&phone=")
            sb.append(pNumber)
            val intent = Intent("android.intent.action.VIEW", Uri.parse(sb.toString()))
            mContext.startActivity(intent)
        } catch (unused: ActivityNotFoundException) {
            Toast.makeText(mContext, R.string.update_whatsapp, Toast.LENGTH_SHORT).show()
        } catch (unused2: UnsupportedEncodingException) {
            Log.i(TAG, unused2.toString())
        }

    }

    fun openWebsite(mContext: Context, link: String) {
        try {
            val uri = Uri.parse(link) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mContext.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun openMail(mContext: Context, emailId: String) {
        try {
            try {
                val i = Intent(Intent.ACTION_SENDTO)
                i.data = Uri.parse("mailto:$emailId")
                try {
                    mContext.startActivity(Intent.createChooser(i, "Send mail..."))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(mContext, mContext.getString(R.string.no_mail_client), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i(TAG, e.toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


}