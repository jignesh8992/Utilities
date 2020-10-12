@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 *  Check if enter email address is valid or not
 *
 * @return true if valid otherwise false
 */

@Suppress("unused")
fun isValidEmail(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}


/**
 *  Check if enter password is match with password criteria
 *
 * @return true if matched otherwise false
 */
@Suppress("unused")
fun isValidPassword(password: String): Boolean {
    val pattern: Pattern
    val matcher: Matcher
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(passwordPattern)
    matcher = pattern.matcher(password)
    return matcher.matches()
}