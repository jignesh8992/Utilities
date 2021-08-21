package com.example.jdrodi.dateedittext

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


private const val DATE_PATTERN = "^[0-9]{2}[-][0-9]{2}[-][0-9]{4}$"

fun String.toDate(format: String): Date? {
    val sdf = SimpleDateFormat(format, Locale.US)
    return sdf.parse(this)
}


fun Date.toString(format: String): String {
    val sdf = SimpleDateFormat(format, Locale.US)
    return sdf.format(this)
}

/**
 * Validate date format with regular expression

 * @return true valid date format, false invalid date format
 */
fun String.isValidateDate(): Boolean {
    return Pattern.compile(DATE_PATTERN).matcher(this).matches()
}

