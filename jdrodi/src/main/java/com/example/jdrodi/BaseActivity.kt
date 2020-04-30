package com.example.jdrodi

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.jdrodi.utilities.SPHelper


/**
 * BaseActivity.kt - A simple class contains some modifications to the native Activity.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */

abstract class BaseActivity : FragmentActivity(), View.OnClickListener {

    lateinit var mContext: Activity // Context of the current activity
    lateinit var sp: SPHelper // Obj. of SharedPreference

    // variable to track event time
    var mLastClickTime: Long = 0
    val mMinDuration = 1000

    // Preventing multiple clicks, using threshold of mMinDuration second
    /* if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
         return
     }
     mLastClickTime = SystemClock.elapsedRealtime()*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = getContext()
        sp = SPHelper(mContext)
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
    abstract fun initAds()

    /**
     * ToDo. Use this method to initialize data to view components.
     */
    abstract fun initData()

    /**
     * ToDo. Use this method to initialize action on view components.
     */
    abstract fun initActions()


}
