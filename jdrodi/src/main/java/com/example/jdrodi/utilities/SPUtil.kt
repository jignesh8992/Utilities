@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * SPHelper.kt - A simple SharedPreferences helper class.
 * @author Jignesh N Patel
 * @date 08-11-2019
 */
@Suppress("unused")
class SPUtil(mContext: Context) {


    // Preferences name to bes used for save and read value from sp

    // SP to be save & retrieve
    val isServiceEnable = "isServiceEnable"
    val userSelection = "userSelection"
    val isPermanentDenied = "isPermanentDenied"


    // Default value when sp is null
    private val defLong: Long = 0
    private val defInt = 0
    private val defFloat = 1.0f
    private val defTrue = true
    private val defFalse = false
    private val defString: String? = null
    private val sp: SharedPreferences

    init {
        sp = SharedPreferences(mContext)
    }


    // ToDo Save and get boolean value

    /**
     *  Save boolean value
     *
     * @param key   The key to store value in sp
     * @param value The boolean value is to save
     */
    fun save(key: String, value: Boolean) {
        sp.save(key, value)
    }

    /**
     *  Get boolean value
     *
     * @param key The key to get value from sp
     * @return The boolean value
     */
    fun getBoolean(key: String): Boolean {
        return sp.read(key, defTrue)
    }

    /**
     *  Get boolean value
     *
     * @param key      The key to get value from sp
     * @param defValue The defValue to return
     * @return The boolean value if saved, or defValue
     */

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.read(key, defValue)
    }

    // ToDo Save and get string value

    /**
     *  Save string value
     *
     * @param key   The key to store value in sp
     * @param value The string value is to save
     */
    fun save(key: String, value: String) {
        sp.save(key, value)
    }

    /**
     *  Get string value
     *
     * @param key The key to get value from sp
     * @return The string value
     */
    fun getString(key: String): String? {
        return sp.read(key, defString!!)
    }

    /**
     *  Get string value
     *
     * @param key      The key to get value from sp
     * @param defValue The defValue to return
     * @return The string value if saved, or defValue
     */
    fun getString(key: String, defValue: String): String? {
        return sp.read(key, defValue)
    }

    // ToDo Save and get Integer value

    /**
     *  Save integer value
     *
     * @param key   The key to store value in sp
     * @param value The integer value is to save
     */
    fun save(key: String, value: Int) {
        sp.save(key, value)
    }

    /**
     *  Get integer value
     *
     * @param key The key to get value from sp
     * @return The integer value
     */
    fun getInt(key: String): Int {
        return sp.read(key, defInt)
    }

    /**
     *  Get integer value
     *
     * @param key      The key to get value from sp
     * @param defValue The defValue to return
     * @return The integer value if saved, or defValue
     */
    fun getInt(key: String, defValue: Int): Int {
        return sp.read(key, defValue)
    }

    // ToDo Save and get Long value

    /**
     *  Save long value
     *
     * @param key   The key to store value in sp
     * @param value The long value is to save
     */
    fun save(key: String, value: Long) {
        sp.save(key, value)
    }

    /**
     *  Get long value
     *
     * @param key The key to get value from sp
     * @return The long value
     */
    fun getLong(key: String): Long {
        return sp.read(key, defLong)
    }

    /**
     *  Get long value
     *
     * @param key      The key to get value from sp
     * @param defValue The defValue to return
     * @return The long value if saved, or defValue
     */
    fun getLong(key: String, defValue: Long): Long {
        return sp.read(key, defValue)
    }

    // ToDo Save and get float value

    /**
     *  Save float value
     *
     * @param key   The key to store value in sp
     * @param value The float value is to save
     */
    fun save(key: String, value: Float) {
        sp.save(key, value)
    }

    /**
     *  Get float value
     *
     * @param key The key to get value from sp
     * @return The float value
     */
    fun getFloat(key: String): Float {
        return sp.read(key, defFloat)
    }

    /**
     *  Get float value
     *
     * @param key      The key to get value from sp
     * @param defValue The defValue to return
     * @return The float value if saved, or defValue
     */
    fun getFloat(key: String, defValue: Float): Float {
        return sp.read(key, defValue)
    }

    // ToDo Save and get string value

    /**
     *  Save bitmap image
     *
     * @param key    The key to store bitmap image in sp
     * @param bitmap The bitmap image to save
     */
    fun saveBitmap(key: String, bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
        sp.save(key, encodedImage)
    }


    /**
     *  Get Bitmap
     *
     * @param key The key to get value from sp
     * @return The bitmap image if saved, or null
     */
    fun getBitmap(key: String): Bitmap? {
        val bitmap: Bitmap?
        val previouslyEncodedImage = sp.read(key, "")
        if (!previouslyEncodedImage!!.equals("", ignoreCase = true)) {
            val b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
        } else {
            bitmap = null
        }
        return bitmap
    }


    /**
     *  Save and get ArrayList
     * implementation 'com.google.code.gson:gson:2.8.4'
     */

    /**
     *  Save ArrayList
     *
     * @param key  The key to store value in sp
     * @param list The arraylist is to save
     */

    /* public void saveArrayList(String key, ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        sp.save(key, json);
    }*/


    /**
     *  Get ArrayList
     *
     * @param key The key to get arraylist from sp
     * @return The bitmap arraylist if saved, or null
     */
    /* public ArrayList<Word> getArrayList(String key) {
        Gson gson = new Gson();
        String json = sp.read(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/


    /*  fun getKBResponse(): KeyboardTheme {
          val gson = Gson()
          val json = sp.read(kbTheme, null)
          return gson.fromJson<KeyboardTheme>(json, KeyboardTheme::class.java)
      }


      fun saveKBResponse(body: KeyboardTheme) {
          val gson = Gson()
          val json = gson.toJson(body)
          save(kbTheme, json)
      }*/


    /**
     *   SharedPreferences helper class
     */
    private inner class SharedPreferences//  Default constructor
    internal constructor(private val mContext: Context) {
        private val myPreferences = "JNP_pref"

        //  Save Integer value
        fun save(key: String, value: Int) {
            val editor =
                mContext.getSharedPreferences(myPreferences, Context.MODE_MULTI_PROCESS).edit()
            editor.putInt(key, value)
            editor.apply()
        }

        //  Save Long value
        fun save(key: String, value: Long) {
            val editor = mContext.getSharedPreferences(myPreferences, 0).edit()
            editor.putLong(key, value)
            editor.apply()
        }

        //  Save Float value
        fun save(key: String, value: Float) {
            val editor = mContext.getSharedPreferences(myPreferences, 0).edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        //  Save String value
        fun save(key: String, value: String) {
            val editor = mContext.getSharedPreferences(myPreferences, 0).edit()
            editor.putString(key, value)
            editor.apply()
        }

        //  Save boolean value
        fun save(key: String, value: Boolean) {
            val editor = mContext.getSharedPreferences(myPreferences, 0).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }


        //  Read String value
        fun read(key: String, defValue: String): String? {
            return mContext.getSharedPreferences(myPreferences, 0).getString(key, defValue)
        }

        //  Read Integer value
        fun read(key: String, defValue: Int): Int {
            return mContext.getSharedPreferences(myPreferences, 0).getInt(key, defValue)
        }

        //  Read Long value
        fun read(key: String, defValue: Long): Long {
            return mContext.getSharedPreferences(myPreferences, 0).getLong(key, defValue)
        }

        //  Read Float value
        fun read(key: String, defValue: Float): Float {
            return mContext.getSharedPreferences(myPreferences, 0).getFloat(key, defValue)
        }

        //  Read Boolean value
        fun read(key: String, defValue: Boolean): Boolean {
            return mContext.getSharedPreferences(myPreferences, 0).getBoolean(key, defValue)
        }

    }

}
