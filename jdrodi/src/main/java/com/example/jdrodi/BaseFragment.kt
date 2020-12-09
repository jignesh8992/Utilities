package com.example.jdrodi

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.jdrodi.utilities.SPUtil


/**
 * BaseFragment.kt - A simple class contains some modifications to the native fragment.
 * @author  Jignesh N Patel
 * @date 24-12-2019
 */

abstract class BaseFragment : Fragment(), View.OnClickListener {


    lateinit var mContext: FragmentActivity // Context of the current activity
    lateinit var sp: SPUtil // Obj. of SharedPreference

    // variable to track event time
    var mLastClickTime: Long = 0
    var mMinDuration = 1000


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity!!
        sp = SPUtil(mContext)
        initViews()
        initAds()
        initData()
        initActions()
    }

    abstract fun getLayoutId(): Int

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

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }
}
