package com.example.jdrodi.callback


/**
 * RVClickListener.kt - A simple interface to get callback on recyclerview item click.
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */
interface RVClickListener {
    fun onItemClick(position: Int)
    fun onEmpty() {}
    fun onNotEmpty() {}
}