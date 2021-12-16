package com.example.jdrodi.utilities

import android.view.View

class ViewUtils

fun View.selected(isSelected: Boolean = true) {
    this.post { this.isSelected = isSelected }
}

internal inline val View.selected
    get() = this.let {
        this.post { this.isSelected = isSelected }
    }

internal inline val View.visible
    get() = this.let {
        visibility = View.VISIBLE
    }

internal inline val View.gone
    get() = this.let {
        visibility = View.GONE
    }

internal inline val View.invisible
    get() = this.let {
        visibility = View.INVISIBLE
    }