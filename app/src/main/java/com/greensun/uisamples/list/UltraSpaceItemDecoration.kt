package com.greensun.uisamples.list

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 万用recyclerView分隔线，支持linear grid staggered LayoutManager
 * 支持横竖向、跨列等情况，支持边缘、横纵向分隔线不同宽度
 * @author greensun
 * @since 2023/6/13
 * blog https://juejin.cn/user/3263006244363095
 */
open class UltraSpaceItemDecoration protected constructor() : RecyclerView.ItemDecoration() {
    private val TAG = UltraSpaceItemDecoration::class.java.simpleName

    // 主轴方向分割线宽度
    protected var mainWidth = 0

    // 交叉轴方向分割线宽度
    protected var crossWidth = 0

    // 边缘宽度
    protected var mainPadding = 0
    protected var crossPadding = 0
    // 哪些item需要忽略间隔
    protected var ignorePredict: IgnorePredict? = null


    open class Builder {
        protected open val itemDecoration: UltraSpaceItemDecoration = UltraSpaceItemDecoration()

        fun dividerWidth(mainWidth: Int, crossWidth: Int): Builder {
            itemDecoration.mainWidth = mainWidth
            itemDecoration.crossWidth = crossWidth
            return this
        }

        fun padding(mainPadding: Int, crossPadding: Int): Builder {
            itemDecoration.mainPadding = mainPadding
            itemDecoration.crossPadding = crossPadding
            return this
        }

        fun ignore(predict: IgnorePredict?): Builder {
            itemDecoration.ignorePredict = predict
            return this
        }

        fun build(): RecyclerView.ItemDecoration {
            return itemDecoration
        }
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val manager = parent.layoutManager
        val adapter = parent.adapter ?: return
        val size = adapter.itemCount
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return
        if (ignorePredict != null) {
            if (ignorePredict!!.ignore(position)) {
                return
            }
        }
        if (manager is LinearLayoutManager) {
            val isVertical = manager.orientation == LinearLayoutManager.VERTICAL
            // grid
            if (manager is GridLayoutManager) {
                // 列数
                val spanCount = manager.spanCount
                // 该item占了几列
                val spanSize = manager.spanSizeLookup.getSpanSize(position)
                // 该item在第几列
                val spanIndex = manager.spanSizeLookup.getSpanIndex(position, spanCount)
                val groupIndex = manager.spanSizeLookup.getSpanGroupIndex(position, spanCount)
                val lastGroupIndex = manager.spanSizeLookup.getSpanGroupIndex(size - 1, spanCount)
                getItemMainOffsets(outRect, isVertical, groupIndex == 0, groupIndex == lastGroupIndex)
                getItemCrossOffsets(outRect, isVertical, spanCount, spanIndex, spanSize)
                return
            }
            getItemMainOffsets(outRect, isVertical, position == 0, position == size - 1)
            if (isVertical) {
                outRect.left = crossPadding
                outRect.right = crossPadding
            } else {
                outRect.top = crossPadding
                outRect.bottom = crossPadding
            }
        } else if (manager is StaggeredGridLayoutManager) {
            val isVertical = manager.orientation == StaggeredGridLayoutManager.VERTICAL
            val lp = view.layoutParams
            if (lp is StaggeredGridLayoutManager.LayoutParams) {
                val spanCount = manager.spanCount
                // 前面没有跨列item时当前item的期望下标
                val exceptSpanIndex = position % spanCount
                // 真实的item下标
                val spanIndex = lp.spanIndex
                // position原属于第一行并且此item之前没有跨列的情况，当前item才属于第一行
                val isFirstGroup = position < spanCount && exceptSpanIndex == spanIndex
                var isLastGroup = false
                if (size - position <= spanCount) {
                    // position原属于最后一行
                    val lastItemView = manager.findViewByPosition(size - 1)
                    if (lastItemView != null) {
                        val lastLp = lastItemView.layoutParams
                        if (lastLp is StaggeredGridLayoutManager.LayoutParams) {
                            // 列表最后一个item和当前item的spanIndex差等于position之差说明它们之间没有跨列的情况，当前item属于最后一行
                            if (lastLp.spanIndex - spanIndex == size - 1 - position) {
                                isLastGroup = true
                            }
                        }
                    }
                }
                val spanSize = if (lp.isFullSpan) spanCount else 1
                getItemMainOffsets(outRect, isVertical, isFirstGroup, isLastGroup)
                getItemCrossOffsets(outRect, isVertical, spanCount, spanIndex, spanSize)
            }
        }
    }

    /**
     * 主轴间隔
     */
    private fun getItemMainOffsets(outRect: Rect, isVertical: Boolean, isFirstGroup: Boolean, isLastGroup: Boolean) {
        if (isFirstGroup) {
            // 是第一行
            if (isVertical) {
                outRect.top = mainPadding
            } else {
                outRect.left = mainPadding
            }
        } else if (isLastGroup) {
            // 是最后一行要加边缘
            if (isVertical) {
                outRect.top = mainWidth
                outRect.bottom = mainPadding
            } else {
                outRect.left = mainWidth
                outRect.right = mainPadding
            }
        } else {
            if (isVertical) {
                outRect.top = mainWidth
            } else {
                outRect.left = mainWidth
            }
        }
    }

    /**
     * 交叉轴间隔
     * [spanIndex] 当前item的以第几列开始
     * [spanSize] 当前item占用的列数
     */
    private fun getItemCrossOffsets(outRect: Rect, isVertical: Boolean, spanCount: Int, spanIndex: Int, spanSize: Int) {
        // 每列占用的间隔
        val spanUsedWidth = (crossPadding * 2 + crossWidth * (spanCount - 1)) / spanCount
        // 到当前item的左边为止的总间隔 - 到上一个item为止需要使用的总间隔
        val lt = crossWidth * spanIndex + crossPadding - spanUsedWidth * spanIndex
        // 到当前item为止需要使用的总间隔 - 到当前item右边为止的总间隔
//        val rb = spanUsedWidth * (spanIndex + spanSize) - crossWidth * (spanIndex + spanSize - 1) - crossPadding
        // 当前item需要使用的总间隔 - 当前item已经使用的总间隔
        val rb = spanUsedWidth * spanSize - crossWidth * (spanSize - 1) - lt
        if (isVertical) {
            outRect.left = lt
            outRect.right = rb
        } else {
            outRect.top = lt
            outRect.bottom = rb
        }
    }

    /**
     * 忽略某些item
     */
    interface IgnorePredict {
        fun ignore(position: Int): Boolean
    }
}