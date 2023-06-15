package com.greensun.uisamples.list

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.greensun.uisamples.R
import kotlin.random.Random


class CommonDecorationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_common_decoration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val random = Random(System.currentTimeMillis())
        val list = mutableListOf<Int>()
        repeat(50) {
            val spanSize = random.nextInt(1, 5)
            list.add(spanSize)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView.itemDecorationCount != 0) {
            recyclerView.removeItemDecorationAt(0)
        }
        recyclerView.addItemDecoration(
            SmartItemDecoration.Builder()
                .dividerWidth(50, 50)
                .edge(60, 60)
                .build()
        )
        val adapter = object : BaseQuickAdapter<Int, QuickViewHolder>(list) {
            override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Int?) {
                holder.setText(R.id.tv, position.toString())
            }

            override fun onCreateViewHolder(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): QuickViewHolder {
                val view = layoutInflater.inflate(R.layout.item_white_block, null)
                return QuickViewHolder(view)
            }
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 5).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return list[position]
                }
            }
        }
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = CommonDecorationFragment()
    }
}