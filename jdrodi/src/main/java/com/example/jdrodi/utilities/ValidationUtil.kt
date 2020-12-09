@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Toast.kt - A simple class for validations.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */


/**
 *  Check if enter email address is valid or not
 *
 * @return true if valid otherwise false
 */

@Suppress("unused")
fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


/**
 *  Check if enter password is match with password criteria
 *
 * @return true if matched otherwise false
 */
@Suppress("unused")
fun String.isValidPassword(): Boolean {
    val pattern: Pattern
    val matcher: Matcher
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(passwordPattern)
    matcher = pattern.matcher(this)
    return matcher.matches()
}