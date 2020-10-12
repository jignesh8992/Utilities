@file:Suppress("unused")

package com.example.jdrodi.utilities

import com.example.jdrodi.R

/**
 * GradientUtil.kt - A simple class to get list of gradients drawable
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */

fun getGradients(): MutableList<Gradient> {
    val gradients: MutableList<Gradient> = mutableListOf()
    gradients.add(Gradient(R.drawable.gradient_13, R.color.gd13color1, R.color.gd13color2))
    gradients.add(Gradient(R.drawable.gradient_14, R.color.gd14color1, R.color.gd14color2))
    gradients.add(Gradient(R.drawable.gradient_15, R.color.gd15color1, R.color.gd15color2))
    gradients.add(Gradient(R.drawable.gradient_16, R.color.gd16color1, R.color.gd16color2))
    gradients.add(Gradient(R.drawable.gradient_17, R.color.gd17color1, R.color.gd17color2))
    gradients.add(Gradient(R.drawable.gradient_18, R.color.gd18color1, R.color.gd18color2))
    gradients.add(Gradient(R.drawable.gradient_19, R.color.gd19color1, R.color.gd19color2))
    gradients.add(Gradient(R.drawable.gradient_20, R.color.gd20color1, R.color.gd20color2))
    gradients.add(Gradient(R.drawable.gradient_21, R.color.gd21color1, R.color.gd21color2))
    gradients.add(Gradient(R.drawable.gradient_10, R.color.gd10color1, R.color.gd10color2))
    gradients.add(Gradient(R.drawable.gradient_11, R.color.gd11color1, R.color.gd11color2))
    gradients.add(Gradient(R.drawable.gradient_12, R.color.gd12color1, R.color.gd12color2))
    return gradients
}

data class Gradient(var gradient: Int, var color1: Int, var color2: Int)