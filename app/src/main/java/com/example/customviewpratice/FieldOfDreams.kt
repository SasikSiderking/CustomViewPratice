package com.example.customviewpratice

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.min
import kotlin.random.Random

class FieldOfDreams @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val centerX by lazy { width / 2 }
    private val centerY by lazy { height / 2 }
    private val radius by lazy { min(width, height) / 3f }
    private val shapeBounds by lazy {
        RectF(
            centerX - radius, centerY - radius, centerX + radius,
            centerY + radius
        )
    }
    var numberOfSectors = 7
    private val anglePerSector: Float = 360f / numberOfSectors
    var sectorArray = Array(numberOfSectors) { i -> getDefaultSector(i) }
    private var axisAngle = 360

    private var text = ""
    var textHeight = 48f
    private val textColor = Color.rgb(0, 0, 0)
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textAlign = Paint.Align.CENTER
        color = textColor
        textSize = textHeight
    }

    private val pointerPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 10f
        textAlign = Paint.Align.CENTER
        color = textColor
        textSize = textHeight
    }
    var pointerLength = 50

    private val decelerateInterpolator = DecelerateInterpolator()

    private var loadImageCallback: LoadImageCallback? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)

        val minh: Int = suggestedMinimumHeight + paddingBottom + paddingTop
        val h: Int = resolveSizeAndState(minh, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {

            drawText(text, centerX.toFloat(), centerY.toFloat() - (radius + textHeight), textPaint)

            for (i in 0 until numberOfSectors) {
                drawArc(
                    shapeBounds,
                    axisAngle - (i + 1) * anglePerSector,
                    anglePerSector,
                    true, sectorArray[i].paint
                )
            }

            drawLine(
                centerX.toFloat() + radius - pointerLength,
                centerY.toFloat(),
                centerX.toFloat() + radius,
                centerY.toFloat(),
                pointerPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            performClick()
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        rotate()
        return super.performClick()
    }

    private fun rotate() {
        val rotationAngle = Random.nextInt(0, 7200)
        ObjectAnimator.ofInt(this, "axisAngle", this.axisAngle, rotationAngle).apply {
            duration = rotationAngle.toLong()
            interpolator = decelerateInterpolator
            addListener(AnimListener())
            start()
        }
    }

    private fun setAxisAngle(newAxisAngle: Int) {
        axisAngle = newAxisAngle
        invalidate()
    }

    private fun getDefaultSector(sectorIndex: Int): Sector {
        return when (sectorIndex) {
            0 -> Sector("Red", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(255, 0, 0)
                style = Paint.Style.FILL
            })

            1 -> Sector("Orange", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(255, 165, 0)
                style = Paint.Style.FILL
            })

            2 -> Sector("Yellow", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(255, 255, 0)
                style = Paint.Style.FILL
            })

            3 -> Sector("Green", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(0, 255, 0)
                style = Paint.Style.FILL
            })

            4 -> Sector("Light blue", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(0, 191, 255)
                style = Paint.Style.FILL
            })

            5 -> Sector("Blue", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(0, 0, 255)
                style = Paint.Style.FILL
            })

            6 -> Sector("Violet", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(143, 0, 255)
                style = Paint.Style.FILL
            })

            else -> {
                Sector("Random", imageUrl, Paint(ANTI_ALIAS_FLAG).apply {
                    color = Color.rgb(
                        Random.nextInt(0, 255),
                        Random.nextInt(0, 255),
                        Random.nextInt(0, 255)
                    )
                    style = Paint.Style.FILL
                })
            }
        }
    }

    private fun resolveChosenColor() {
        val axisModule = axisAngle % 360
        val colorNumber = (axisModule / anglePerSector).toInt()
        if (colorNumber % 2 != 0) {
            loadImageCallback?.onLoadImage(sectorArray[colorNumber].imageUrl)
        } else {
            text = sectorArray[colorNumber].name
            invalidate()
        }
    }

    fun setLoadImageCallback(callback: LoadImageCallback) {
        this.loadImageCallback = callback
    }

    data class Sector(
        val name: String,
        val imageUrl: String,
        val paint: Paint
    )

    private inner class AnimListener: AnimatorListener{
        override fun onAnimationStart(animation: Animator) {
            text = ""
            invalidate()
        }

        override fun onAnimationEnd(animation: Animator) {
            resolveChosenColor()
        }

        override fun onAnimationCancel(animation: Animator) {
            TODO("Not yet implemented")
        }

        override fun onAnimationRepeat(animation: Animator) {
            TODO("Not yet implemented")
        }

    }

    private companion object {
        const val imageUrl = "https://picsum.photos/200/300"
    }
}