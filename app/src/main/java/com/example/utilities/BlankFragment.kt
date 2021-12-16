package com.example.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.jdrodi.base.BaseVBFragment
import com.example.utilities.databinding.FragmentBlankBinding


class BlankFragment : BaseVBFragment<FragmentBlankBinding>() {

    override val mActivity: FragmentActivity
        get() = requireActivity()


    override fun initData() {

    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBlankBinding
        get() = FragmentBlankBinding::inflate


}