package com.greensun.uisamples.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.greensun.uisamples.R
import com.greensun.uisamples.ViewBindingFragment
import com.greensun.uisamples.databinding.FragmentRecyclerViewEntryBinding

class RecyclerViewEntryFragment : ViewBindingFragment<FragmentRecyclerViewEntryBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.gridList.setOnClickListener {
            findNavController().navigate(R.id.grid_list_fragment)
        }

        mViewBinding.staggeredList.setOnClickListener {
            findNavController().navigate(R.id.stagger_list_fragment)
        }

        mViewBinding.wave.setOnClickListener {
            findNavController().navigate(R.id.wave_fragment)
        }
    }
}