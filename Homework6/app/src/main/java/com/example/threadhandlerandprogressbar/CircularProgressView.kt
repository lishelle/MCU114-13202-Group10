package com.example.threadhandlerandprogressbar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0
    private val max = 100

    private val strokeWidthPx = 40f  // 🔹 粗一點的圓環

    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#D9D9D9") // 淺灰底
        style = Paint.Style.STROKE
        strokeWidth = strokeWidthPx
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidthPx
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val rect = RectF()
    private var gradient: SweepGradient? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val radius = (minOf(w, h) / 2f) - (strokeWidthPx / 2)
        rect.set(
            w / 2f - radius,
            h / 2f - radius,
            w / 2f + radius,
            h / 2f + radius
        )

        // 🔹設定漸層顏色：淺藍綠 → 藍 → 深藍 → 黑

        gradient = SweepGradient(
            w / 2f, h / 2f,
            intArrayOf(
                Color.parseColor("#00E0C6"), // 淺藍綠
                Color.parseColor("#0080FF"), // 藍
                Color.parseColor("#003366"), // 深藍
                Color.parseColor("#000000")  // 黑
            ),
            floatArrayOf(0f, 0.4f, 0.8f, 1f)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 背景圓圈
        canvas.drawArc(rect, 0f, 360f, false, backgroundPaint)

        // 應用漸層
        progressPaint.shader = gradient

        // 進度弧線
        val sweepAngle = 360f * progress / max
        canvas.save()
        canvas.rotate(-90f, width / 2f, height / 2f) // 從上方開始
        canvas.drawArc(rect, 0f, sweepAngle, false, progressPaint)
        canvas.restore()

        // 百分比文字
        val percentText = "$progress%"
        val xPos = width / 2f
        val yPos = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(percentText, xPos, yPos, textPaint)
    }

    fun setProgress(value: Int) {
        progress = value.coerceIn(0, max)
        invalidate()
    }
}
