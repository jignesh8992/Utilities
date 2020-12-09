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


private val TAG = "IntentActions"

@SuppressLint("MissingPermission")
fun Context.makeCall(pNumber: String) {
    try {
        val intent = Intent(ACTION_CALL)
        intent.data = Uri.parse("tel:$pNumber")
        startActivity(intent)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
}

fun Context.openWhatsApp(pNumber: String) {
    try {
        val textMsg = ""
        val sb = StringBuilder()
        sb.append("whatsapp://send/?text=")
        sb.append(URLEncoder.encode(textMsg, "UTF-8"))
        sb.append("&phone=")
        sb.append(pNumber)
        val intent = Intent("android.intent.action.VIEW", Uri.parse(sb.toString()))
        startActivity(intent)
    } catch (unused: ActivityNotFoundException) {
        Toast.makeText(this, R.string.update_whatsapp, Toast.LENGTH_SHORT).show()
    } catch (unused2: UnsupportedEncodingException) {
        Log.i(TAG, unused2.toString())
    }

}

fun Context.openWebsite(link: String) {
    try {
        val uri = Uri.parse(link) // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
}

fun Context.openMail( emailId: String) {
    try {
        try {
            val i = Intent(Intent.ACTION_SENDTO)
            i.data = Uri.parse("mailto:$emailId")
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, getString(R.string.no_mail_client), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, e.toString())
        }
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
}