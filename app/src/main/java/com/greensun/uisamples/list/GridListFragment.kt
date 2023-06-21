package com.greensun.uisamples.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.greensun.uisamples.R
import com.greensun.uisamples.ViewBindingFragment
import com.greensun.uisamples.databinding.FragmentGridListBinding
import kotlin.random.Random


class GridListFragment : ViewBindingFragment<FragmentGridListBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val random = Random(System.currentTimeMillis())
        val list = mutableListOf<Int>()
        repeat(50) {
            val spanSize = random.nextInt(1, 5)
            list.add(spanSize)
        }
        if (mViewBinding.recyclerView.itemDecorationCount != 0) {
            mViewBinding.recyclerView.removeItemDecorationAt(0)
        }
        mViewBinding.recyclerView.addItemDecoration(
            UltraSpaceItemDecoration.Builder()
                .dividerWidth(20, 40)
                .padding(60, 60)
                .build()
        )
        val adapter = object : BaseQuickAdapter<Int, QuickViewHolder>(list) {
            override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Int?) {
                holder.setText(R.id.tv, position.toString())
                val layoutManager = mViewBinding.recyclerView.layoutManager
                if (layoutManager is GridLayoutManager) {
                    if (layoutManager.orientation == RecyclerView.VERTICAL) {
                        val lp = GridLayoutManager.LayoutParams(
                            GridLayoutManager.LayoutParams.MATCH_PARENT, 300
                        )
                        holder.itemView.layoutParams = lp
                    } else {
                        val lp = GridLayoutManager.LayoutParams(
                            300, GridLayoutManager.LayoutParams.MATCH_PARENT
                        )
                        holder.itemView.layoutParams = lp
                    }
                }
            }

            override fun onCreateViewHolder(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): QuickViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.item_block, null)
                return QuickViewHolder(view)
            }

        }
        mViewBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 5).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return list[position]
                }
            }
        }
        mViewBinding.recyclerView.adapter = adapter

        mViewBinding.reverse.setOnClickListener {
            mViewBinding.recyclerView.layoutManager?.let {
                if (it is LinearLayoutManager) {
                    val orientation = if (it.orientation == RecyclerView.VERTICAL)
                        RecyclerView.HORIZONTAL else RecyclerView.VERTICAL
                    val layoutManager = GridLayoutManager(requireContext(), 5, orientation, false).apply {
                        spanSizeLookup = object : SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return list[position]
                            }
                        }
                    }
                    mViewBinding.recyclerView.layoutManager = layoutManager
                }
            }
        }
    }
}