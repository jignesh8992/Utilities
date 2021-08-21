package com.example.jdrodi.utilities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.jdrodi.R
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil.showKeyboard
import java.lang.reflect.Field

/**
 * @author:  Jignesh N Patel
 * @date: 28-Feb-2021 1:30 PM
 */

fun SearchView.clearSearchViewFocus() {
    clearFocus()
    rootView.requestFocus()
}

fun SearchView.initSearchView(hint: String, textHintColor: Int = R.color.grey_400, textColor: Int = R.color.black) {
    this.setIconifiedByDefault(false)
    this.isFocusable = true
    this.isIconified = false
    this.requestFocusFromTouch()
    this.isSubmitButtonEnabled = false
    this.onActionViewExpanded()
    val txtSearch: EditText = this.findViewById(R.id.search_src_text)
    txtSearch.hint = hint
    txtSearch.setHintTextColor(ContextCompat.getColor(this.context, textHintColor))
    txtSearch.setTextColor(ContextCompat.getColor(this.context, textColor))
    this.clearFocus()
}


@SuppressLint("CutPasteId", "SoonBlockedPrivateApi")
fun Context.initSearchView(searchView: SearchView, searchHint: String, textHintColor: Int = R.color.grey_400, textColor: Int = R.color.black, cancelIcon: Int? = null) {
    searchView.setIconifiedByDefault(true)
    searchView.isFocusable = true
    searchView.isIconified = false
    searchView.requestFocusFromTouch()
    searchView.isSubmitButtonEnabled = false
    searchView.onActionViewExpanded()
    val closeButton: ImageView = searchView.findViewById(R.id.search_close_btn)
    if (cancelIcon != null)
        closeButton.setImageResource(cancelIcon)
    val txtSearch: EditText = searchView.findViewById(R.id.search_src_text)
    txtSearch.hint = searchHint
    txtSearch.textSize = resources.getDimension(R.dimen._5ssp)
    txtSearch.setHintTextColor(ContextCompat.getColor(this, textHintColor))
    txtSearch.setTextColor(ContextCompat.getColor(this, textColor))
    try {
        val searchTextView: AutoCompleteTextView = searchView.findViewById(R.id.search_src_text)
        val mCursorDrawableRes: Field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        mCursorDrawableRes.isAccessible = true
        mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor) //This sets the
        // cursor resource ID to 0 or @null which will make it visible on white background
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    searchView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (v === searchView) {
            if (hasFocus) {
                // Open keyboard
                (getSystemService(FragmentActivity.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(searchView, InputMethodManager.SHOW_FORCED)
            } else {
                // Close keyboard
                (getSystemService(FragmentActivity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(searchView.windowToken, 0)
            }
        }
    }
    searchView.clearFocus()
}

fun Activity.showSearch(searchView: SearchView, viewSearchView: View, viewToolbar: View) {
    searchView.requestFocus()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        circleReveal(viewSearchView, 1, true, true, viewToolbar)
    } else {
        viewSearchView.visibility = View.VISIBLE
        viewToolbar.visibility = View.GONE
    }
}

fun Activity.hideSearch(searchView: SearchView, viewSearchView: View, viewToolbar: View) {
    searchView.setQuery("", false)
    searchView.clearFocus()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        viewToolbar.visibility = View.VISIBLE
        circleReveal(viewSearchView, 1, true, false)
    } else {
        viewSearchView.visibility = View.INVISIBLE
        viewToolbar.visibility = View.VISIBLE
    }
}


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun Activity.circleReveal(searchView: View, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean, viewToolbar: View? = null) {
    var width = searchView.width
    if (posFromRight > 0) width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources
        .getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2
    if (containsOverflow) width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)
    val cx = width
    val cy = searchView.height / 2
    val anim: Animator = if (isShow) ViewAnimationUtils.createCircularReveal(searchView, cx, cy, 0f, width.toFloat()) else ViewAnimationUtils.createCircularReveal(
        searchView, cx, cy,
        width.toFloat(), 0f
    )
    anim.duration = 220.toLong()
    // make the view invisible when the animation is done
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            if (!isShow) {
                super.onAnimationEnd(animation)
                searchView.visibility = View.INVISIBLE
            } else {
                if (viewToolbar != null) {
                    viewToolbar.visibility = View.INVISIBLE
                }
                searchView.requestFocus()
                showKeyboard()
            }
        }
    })
    // make the view visible and start the animation
    if (isShow) {
        searchView.visibility = View.VISIBLE
    }
    // start the animation
    anim.start()
}