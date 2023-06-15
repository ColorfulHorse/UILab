package com.greensun.uisamples.list

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author greensun
 * @since 2023/6/15
 * blog https://juejin.cn/user/3263006244363095
 */
class SquareLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = if (widthMode == MeasureSpec.EXACTLY) width else height
        setMeasuredDimension(size, size)
    }
}