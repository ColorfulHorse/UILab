package com.greensun.uisamples.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.greensun.uisamples.R
import com.greensun.uisamples.ViewBindingFragment
import com.greensun.uisamples.databinding.FragmentStaggerListBinding
import kotlin.random.Random


class StaggeredListFragment : ViewBindingFragment<FragmentStaggerListBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val random = Random(System.currentTimeMillis())
        val list = mutableListOf<Int>()
        repeat(50) {
            val height = random.nextInt(300, 801)
            list.add(height)
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
                if (layoutManager is StaggeredGridLayoutManager) {
                    if (layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL) {
                        val lp = StaggeredGridLayoutManager.LayoutParams(
                            StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT
                        )

                        val height = item ?: 0
                        lp.isFullSpan = height % 5 == 0
                        lp.height = height
                        holder.itemView.layoutParams = lp

                    } else {
                        val lp = StaggeredGridLayoutManager.LayoutParams(
                            StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT, StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT
                        )

                        val width = item ?: 0
                        lp.isFullSpan = width % 5 == 0
                        lp.width = width
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
        mViewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL)
        mViewBinding.recyclerView.adapter = adapter

        // 切换方向
        mViewBinding.reverse.setOnClickListener {
            mViewBinding.recyclerView.layoutManager?.let {
                if (it is StaggeredGridLayoutManager) {
                    val orientation = if (it.orientation == RecyclerView.VERTICAL)
                        RecyclerView.HORIZONTAL else RecyclerView.VERTICAL
                    val layoutManager = StaggeredGridLayoutManager(5, orientation)
                    mViewBinding.recyclerView.layoutManager = layoutManager
                }
            }
        }
    }
}