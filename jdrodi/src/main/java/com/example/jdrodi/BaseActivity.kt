package com.example.jdrodi

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.jdrodi.jprogress.JProgress
import com.example.jdrodi.utilities.SPUtil


/**
 * BaseActivity.kt - A simple class contains some modifications to the native Activity.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */

abstract class BaseActivity : FragmentActivity(), View.OnClickListener {

    lateinit var mContext: Activity // Context of the current activity
    lateinit var sp: SPUtil // Obj. of SharedPreference
    private var jpDialog: JProgress? = null

    // variable to track event time
    var mLastClickTime: Long = 0
    var mMinDuration = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = getContext()
        sp = SPUtil(mContext)
        jpDialog = JProgress.create(mContext, JProgress.Style.SPIN_INDETERMINATE)
    }

    override fun setContentView(layout: Int) {
        super.setContentView(layout)
        initViews()
        initAds()
        initData()
        initActions()
    }

    /**
     * ToDo. Set the context of activity
     *
     * @return The context of activity.
     */
    abstract fun getContext(): Activity

    /**
     * ToDo. Use this method to setup views.
     */
    open fun initViews() {}

    /**
     * ToDo. Use this method to setup ads.
     */
    open fun initAds() {}

    /**
     * ToDo. Use this method to initialize data to view components.
     */
    abstract fun initData()

    /**
     * ToDo. Use this method to initialize action on view components.
     */
    abstract fun initActions()

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    fun jpShow() {
        jpDialog?.show()
    }

    fun jpDismiss() {
        jpDialog?.dismiss()
    }


}
