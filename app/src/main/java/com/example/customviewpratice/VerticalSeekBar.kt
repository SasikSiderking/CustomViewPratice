package com.example.customviewpratice

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent


class VerticalSeekBar : androidx.appcompat.widget.AppCompatSeekBar {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    private var changeListener: OnSeekBarChangeListener? = null

    override fun setOnSeekBarChangeListener(mListener: OnSeekBarChangeListener) {
        this.changeListener = mListener
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(c: Canvas) {
        c.rotate(270f)
        c.translate(-height.toFloat(), 0f)
        super.onDraw(c)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isSelected = true
                isPressed = true
                if (changeListener != null) changeListener!!.onStartTrackingTouch(this)
            }

            MotionEvent.ACTION_UP -> {
                isSelected = false
                isPressed = false
                if (changeListener != null) changeListener!!.onStopTrackingTouch(this)
            }

            MotionEvent.ACTION_MOVE -> {
                val progress = (max
                        - (max * event.y / height).toInt())
                setProgress(progress)
                onSizeChanged(width, height, 0, 0)
                if (changeListener != null) changeListener!!.onProgressChanged(this, progress, true)
            }

            MotionEvent.ACTION_CANCEL -> {}
        }
        return true
    }
}