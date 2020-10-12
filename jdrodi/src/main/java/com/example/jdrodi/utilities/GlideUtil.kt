@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import java.io.File

/**
 *  Load Image using Glide
 *
 * @param resId   The resource id of image
 * @param imageView The Image view in which image will be load
 * @param thumbnail The thumbnail will show when image is loading
 * @param progressBar The progressBar will show when image is loading
 * @param isCenterCrop The flag which is used for center crop image or center inside
 */
fun Context.loadImage(resId: Int, imageView: ImageView, thumbnail: Int, progressBar: View? = null, isCenterCrop: Boolean = true) {
    val thumbnailRequest = Glide.with(this).load(thumbnail)
    if (progressBar != null) {
        progressBar.visibility = View.VISIBLE
    }

    if (isCenterCrop) {
        Glide.with(this)
            .load(resId)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerCrop()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    } else {
        Glide.with(this)
            .load(resId)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerInside()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    }


}

/**
 *  Load Image using Glide
 *
 * @param imgUrl   The url of image
 * @param imageView The Image view in which image will be load
 * @param thumbnail The thumbnail will show when image is loading
 * @param progressBar The progressBar will show when image is loading
 * @param isCenterCrop The flag which is used for center crop image or center inside
 */
fun Context.loadImage(imgUrl: String, imageView: ImageView, thumbnail: Int, progressBar: View? = null, isCenterCrop: Boolean = true) {
    val thumbnailRequest = Glide.with(this).load(thumbnail)
    if (progressBar != null) {
        progressBar.visibility = View.VISIBLE
    }

    if (isCenterCrop) {
        Glide.with(this)
            .load(imgUrl)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerCrop()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    } else {
        Glide.with(this)
            .load(imgUrl)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerInside()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    }
}

/**
 *  Load Image using Glide
 *
 * @param imgFile   The file of image
 * @param imageView The Image view in which image will be load
 * @param thumbnail The thumbnail will show when image is loading
 * @param progressBar The progressBar will show when image is loading
 * @param isCenterCrop The flag which is used for center crop image or center inside
 */
fun Context.loadImage(imgFile: File, imageView: ImageView, thumbnail: Int, progressBar: View? = null, isCenterCrop: Boolean = true) {
    val thumbnailRequest = Glide.with(this).load(thumbnail)
    if (progressBar != null) {
        progressBar.visibility = View.VISIBLE
    }

    if (isCenterCrop) {
        Glide.with(this)
            .load(imgFile)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerCrop()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    } else {
        Glide.with(this)
            .load(imgFile)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerInside()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    }
}

/**
 *  Load Image using Glide
 *
 * @param imgUri   The uri of image
 * @param imageView The Image view in which image will be load
 * @param thumbnail The thumbnail will show when image is loading
 * @param progressBar The progressBar will show when image is loading
 * @param isCenterCrop The flag which is used for center crop image or center inside
 */
fun Context.loadImage(imgUri: Uri, imageView: ImageView, thumbnail: Int, progressBar: View? = null, isCenterCrop: Boolean = true) {
    val thumbnailRequest = Glide.with(this).load(thumbnail)
    if (progressBar != null) {
        progressBar.visibility = View.VISIBLE
    }

    if (isCenterCrop) {
        Glide.with(this)
            .load(imgUri)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerCrop()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    } else {
        Glide.with(this)
            .load(imgUri)
            .placeholder(thumbnail)
            .error(thumbnail)
            // .thumbnail(0.15f)
            .thumbnail(thumbnailRequest)
            .centerInside()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                    imageView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imageView)
    }
}


/**
 *  Get bitmap from Url using glide
 *
 * @param imagePath   The path of image
 * @return The bitmap retrieve from URL
 */
fun Context.getUrlBitmap(imagePath: String): Bitmap? {
    return Glide.with(this).asBitmap().load(imagePath).submit().get()
}


/**
 *  Get bitmap from Uri using glide
 *
 * @param imageUri   The uri of image
 * @return The bitmap retrieve from URL
 */
fun Context.getUrlBitmap(imageUri: Uri): Bitmap? {
    return Glide.with(this).asBitmap().load(imageUri).submit().get()
}

