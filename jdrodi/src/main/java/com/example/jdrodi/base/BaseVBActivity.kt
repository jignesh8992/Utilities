package com.example.jdrodi.base

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.example.jdrodi.utilities.SPUtil
import com.example.jdrodi.utilities.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * BaseVBActivity.kt - A simple class contains some modifications to the native Activity.
 * @author  Jignesh N Patel
 * @date 16-12-2021
 */

abstract class BaseVBActivity<VB : ViewBinding> : FragmentActivity(), View.OnClickListener, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected val Any.TAG: String
        get() {
            val tag = this::class.java.canonicalName ?: this::class.java.name
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }


    protected abstract val mActivity: Activity

    protected lateinit var mBinding: VB
        private set

    abstract val bindingInflater: (LayoutInflater) -> VB


    lateinit var sp: SPUtil

    // variable to track event time
    var mLastClickTime: Long = 0
    var mMinDuration = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = bindingInflater.invoke(layoutInflater).apply {
            setContentView(root)
        }
        setContentView(mBinding.root)
        sp = SPUtil(mActivity)
        onViewBindingCreated(savedInstanceState)
    }


    override fun setContentView(layout: Int) {
        super.setContentView(layout)
        initViews()
        initAds()
        initData()
        initActions()
    }

    open fun onViewBindingCreated(savedInstanceState: Bundle?) {}


    /**
     * ToDo. Use this method to initialize view components.
     */
    open fun initViews() {}

    /**
     * ToDo. Use this method to load Ads.
     */
    open fun initAds() {}


    /**
     * ToDo. Use this method to initialize data to view components.
     */
    abstract fun initData()

    /**
     * ToDo. Use this method to initialize action on view components.
     */
    open fun initActions() {}

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    @CallSuper
    override fun onDestroy() {
        coroutineContext[Job]?.cancel()
        super.onDestroy()
    }
}