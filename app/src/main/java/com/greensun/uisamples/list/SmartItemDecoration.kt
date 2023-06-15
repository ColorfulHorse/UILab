package com.greensun.uisamples.list

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 给每个item四周画一个三角形
 * @author greensun
 * @since 2023/6/13
 * blog https://juejin.cn/user/3263006244363095
 */
class SmartItemDecoration : SpaceItemDecoration() {

    private val paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
        color = Color.CYAN
        style = Paint.Style.FILL_AND_STROKE
    }

    private val path = Path()

    class Builder : SpaceItemDecoration.Builder() {
        override val itemDecoration = SmartItemDecoration()

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager
        if (manager is LinearLayoutManager) {
            val isVertical = manager.orientation == LinearLayoutManager.VERTICAL
            val horizontalSpace = (if (isVertical) crossWidth else mainWidth) / 2f
            val verticalSpace = (if (isVertical) mainWidth else crossWidth) / 2f
            for (i in 0 until parent.childCount) {
                val view = parent.getChildAt(i)
                val left = view.left.toFloat()
                val top = view.top.toFloat()
                val right = view.right.toFloat()
                val bottom = view.bottom.toFloat()
                drawTriangle(c, PointF(left, top), PointF(right, top), PointF((right + left) / 2f, top - verticalSpace))
                drawTriangle(c, PointF(right, top), PointF(right, bottom), PointF(right + horizontalSpace, (top + bottom) / 2f))
                drawTriangle(c, PointF(right, bottom), PointF(left, bottom), PointF((right + left) / 2f, bottom + verticalSpace))
                drawTriangle(c, PointF(left, top), PointF(left, bottom), PointF(left - horizontalSpace, (top + bottom) / 2f))
            }
        }
    }


    private fun drawTriangle(canvas: Canvas, p1: PointF, p2: PointF, p3: PointF) {
        path.reset()
        path.moveTo(p1.x, p1.y)
        path.lineTo(p2.x, p2.y)
        path.lineTo(p3.x, p3.y)
        path.lineTo(p1.x, p1.y)
        canvas.drawPath(path, paint)
    }
}