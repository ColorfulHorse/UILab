package com.greensun.uisamples

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * @author greensun
 * @since 2023/10/7
 * blog https://juejin.cn/user/3263006244363095
 */
class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val lineWidth = 10

    private val lineHeight = 50

    private val space = 20

    private val padding = 20

    private val lineCount = 12

    private val paint = Paint().apply {
        flags = Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        strokeWidth = lineWidth.toFloat()
        color = Color.RED
        strokeCap = Paint.Cap.ROUND
    }

    var animator: Animator? = null

    var fraction = 0f

    val pts = FloatArray(lineCount * 4)

    init {
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.repeatCount = ValueAnimator.INFINITE
        anim.repeatMode = ValueAnimator.REVERSE
        anim.addUpdateListener {
            fraction = it.animatedValue as Float
            invalidate()
        }
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        animator = anim
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = lineCount * lineWidth + space * (lineCount - 1) + padding * 2
        val height = lineHeight + padding * 2
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        // 第一条线的中线
        var startX = padding + lineWidth / 2f
        val centerY = height / 2f
        (0 until lineCount).forEach {
            // 奇偶相反
            val lineHeight = if (it % 2 == 0) fraction * height else (1f - fraction) * height
            val y0 = centerY + lineHeight / 2f
            val y1 = centerY - lineHeight / 2f
            // 每条线的x0 y0 x1 y1
            pts[it * 4] = startX
            pts[it * 4 + 1] = y0
            pts[it * 4 + 2] = startX
            pts[it * 4 + 3] = y1
            startX += lineWidth + space
        }
        canvas.drawLines(pts, paint)
    }
}