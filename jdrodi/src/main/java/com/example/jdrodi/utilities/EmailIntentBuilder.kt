/*
 * Copyright 2015-2019 cketti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.jdrodi.utilities

import com.example.jdrodi.utilities.EmailIntentBuilder
import android.content.Intent
import android.content.ActivityNotFoundException
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Patterns
import java.lang.StringBuilder
import java.util.LinkedHashSet

/**
 * A helper to create email intents, i.e. [Intent.ACTION_SENDTO] with a `mailto:` URI.
 *
 *
 * Example usage:
 * <pre>
 * `
 * EmailIntentBuilder.from(activity)
 * .to("alice@example.org")
 * .subject("Bug report for 'My awesome app'")
 * .body("Something went wrong :(")
 * .start();
` *
</pre> *
 *
 * This creates an intent containing the following `mailto:` URI:
 * <pre>
 * `
 * mailto:alice@example.org?subject=Bug%20report%20for%20'My%20awesome%20app'&body=Something%20went%20wrong%20%3A(
` *
</pre> *
 *
 * @see .from
 */
class EmailIntentBuilder private constructor(val context: Context) {

    private val to: MutableSet<String> = LinkedHashSet()
    private val cc: MutableSet<String> = LinkedHashSet()
    private val bcc: MutableSet<String> = LinkedHashSet()
    private var subject: String? = null
    private var body: String? = null

    /**
     * Add an email address to be used in the "to" field.
     *
     * @param to
     * the email address to add
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun to(to: String): EmailIntentBuilder {
        checkEmail(to)
        this.to.add(to)
        return this
    }

    /**
     * Add a list of email addresses to be used in the "to" field.
     *
     * @param to
     * the email addresses to add
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun to(to: Collection<String>): EmailIntentBuilder {
        checkNotNull(to)
        for (email in to) {
            checkEmail(email)
        }
        this.to.addAll(to)
        return this
    }

    /**
     * Add an email address to be used in the "cc" field.
     *
     * @param cc
     * the email address to add
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun cc(cc: String): EmailIntentBuilder {
        checkEmail(cc)
        this.cc.add(cc)
        return this
    }

    /**
     * Add an email address to be used in the "cc" field.
     *
     * @param cc
     * the email addresses to add
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun cc(cc: Collection<String>): EmailIntentBuilder {
        checkNotNull(cc)
        for (email in cc) {
            checkEmail(email)
        }
        this.cc.addAll(cc)
        return this
    }

    /**
     * Add an email address to be used in the "bcc" field.
     *
     * @param bcc
     * the email address to add
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun bcc(bcc: String): EmailIntentBuilder {
        checkEmail(bcc)
        this.bcc.add(bcc)
        return this
    }

    /**
     * Add an email address to be used in the "bcc" field.
     *
     * @param bcc
     * the email addresses to add
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun bcc(bcc: Collection<String>): EmailIntentBuilder {
        checkNotNull(bcc)
        for (email in bcc) {
            checkEmail(email)
        }
        this.bcc.addAll(bcc)
        return this
    }

    /**
     * Set the subject line for this email intent.
     *
     * @param subject
     * the email subject line
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun subject(subject: String): EmailIntentBuilder {
        checkNotNull(subject)
        checkNoLineBreaks(subject)
        this.subject = subject
        return this
    }

    /**
     * Set the text body for this email intent.
     *
     * @param body
     * the text body
     *
     * @return This `EmailIntentBuilder` for method chaining
     */
    fun body(body: String): EmailIntentBuilder {
        checkNotNull(body)
        this.body = fixLineBreaks(body)
        return this
    }

    /**
     * Launch the email intent.
     *
     * @return `false` if no activity to handle the email intent could be found; `true` otherwise
     */
    fun start(): Boolean {
        val emailIntent = build()
        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            return false
        }
        return true
    }

    private fun startActivity(intent: Intent) {
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * Build the [Intent.ACTION_SENDTO] intent.
     *
     * @return the intent containing the provided information
     */
    fun build(): Intent {
        val mailtoUri = constructMailtoUri()
        return Intent(Intent.ACTION_SENDTO, mailtoUri)
    }

    private fun constructMailtoUri(): Uri {
        val mailto = StringBuilder(1024)
        mailto.append("mailto:")
        addRecipients(mailto, to)
        var hasQueryParameters: Boolean
        hasQueryParameters = addRecipientQueryParameters(mailto, "cc", cc, false)
        hasQueryParameters = addRecipientQueryParameters(mailto, "bcc", bcc, hasQueryParameters)
        hasQueryParameters = addQueryParameter(mailto, "subject", subject, hasQueryParameters)
        addQueryParameter(mailto, "body", body, hasQueryParameters)
        return Uri.parse(mailto.toString())
    }

    private fun addQueryParameter(mailto: StringBuilder, field: String, value: String?, hasQueryParameters: Boolean): Boolean {
        if (value == null) {
            return hasQueryParameters
        }
        mailto.append(if (hasQueryParameters) '&' else '?').append(field).append('=').append(Uri.encode(value))
        return true
    }

    private fun addRecipientQueryParameters(
        mailto: StringBuilder, field: String, recipients: Set<String>,
        hasQueryParameters: Boolean
    ): Boolean {
        if (recipients.isEmpty()) {
            return hasQueryParameters
        }
        mailto.append(if (hasQueryParameters) '&' else '?').append(field).append('=')
        addRecipients(mailto, recipients)
        return true
    }

    private fun addRecipients(mailto: StringBuilder, recipients: Set<String>) {
        if (recipients.isEmpty()) {
            return
        }
        for (recipient in recipients) {
            mailto.append(encodeRecipient(recipient))
            mailto.append(',')
        }
        mailto.setLength(mailto.length - 1)
    }

    private fun checkEmail(email: String) {
        checkNotNull(email)
        require(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Argument is not a valid email address (according to " +
                    "Patterns.EMAIL_ADDRESS)"
        }
    }

    private fun checkNoLineBreaks(text: String) {
        val containsCarriageReturn = text.indexOf('\r') != -1
        val containsLineFeed = text.indexOf('\n') != -1
        require(!(containsCarriageReturn || containsLineFeed)) { "Argument must not contain line breaks" }
    }

    companion object {
        /**
         * Create a builder to create an [Intent.ACTION_SENDTO] intent or to launch that intent.
         *
         * @param context
         * The `Context` that will be used to launch the intent
         *
         * @return An email intent builder
         */
        fun from(context: Context): EmailIntentBuilder {
            return EmailIntentBuilder(context)
        }

        private fun <T> checkNotNull(`object`: T): T {
            requireNotNull(`object`) { "Argument must not be null" }
            return `object`
        }

        fun encodeRecipient(recipient: String): String {
            val index = recipient.lastIndexOf('@')
            val localPart = recipient.substring(0, index)
            val host = recipient.substring(index + 1)
            return Uri.encode(localPart) + "@" + Uri.encode(host)
        }

        fun fixLineBreaks(text: String): String {
            return text.replace("\r\n".toRegex(), "\n").replace('\r', '\n').replace("\n".toRegex(), "\r\n")
        }
    }

}