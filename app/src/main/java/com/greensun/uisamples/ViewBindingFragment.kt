package com.greensun.uisamples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.Exception

abstract class ViewBindingFragment<VB: ViewBinding> : Fragment() {

    private var viewBinding: VB? = null

    protected val mViewBinding: VB
        get() {
            return viewBinding ?: throw Exception("页面已经被回收")
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = ViewBindingCreator<VB>().onCreateView(javaClass, inflater, container)
        this.viewBinding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}