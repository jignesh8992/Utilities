package com.example.utilities


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.jdrodi.base.BaseVBActivity
import com.example.utilities.databinding.ActivityMainBinding


class MainActivity : BaseVBActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override val mActivity: Activity
        get() = this@MainActivity

    override fun initData() {

    }

}
