package com.yenen.ahmet.basecorelibrary.base.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.yenen.ahmet.basecorelibrary.R
import android.util.DisplayMetrics
import android.graphics.*
import kotlin.math.roundToInt


class ScannerOverlay :ViewGroup {

    private var rectWidth:Int=0
    private var rectHeight:Int =0
    private var lineColor:Int =0
    private var lineWidth:Int =0
    private var frames:Int =0
    private var mLeft:Float=0f
    private var mTop:Float=0f
    private var endY:Float =0f
    private val rect = RectF()
    private var revAnimation:Boolean = false

    private val eraser = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val line = Paint()


    constructor(context: Context, attrs: AttributeSet) : this(context, attrs,0){
        eraser.isAntiAlias = true
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
       val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ScannerOverlay,
            0, 0)

        rectWidth = a.getInteger(R.styleable.ScannerOverlay_square_width, resources.getInteger(R.integer.scanner_rect_width))
        rectHeight = a.getInteger(R.styleable.ScannerOverlay_square_height, resources.getInteger(R.integer.scanner_rect_height))
        lineColor = a.getColor(R.styleable.ScannerOverlay_line_color, ContextCompat.getColor(context, R.color.scanner_line))
        lineWidth = a.getInteger(R.styleable.ScannerOverlay_line_width, resources.getInteger(R.integer.line_width))
        frames = a.getInteger(R.styleable.ScannerOverlay_line_speed, resources.getInteger(R.integer.line_width))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mLeft = ((w - dpToPx(rectWidth)) / 2).toFloat()
        mTop =((h - dpToPx(rectHeight)) / 2).toFloat()
        endY = mTop
        super.onSizeChanged(w, h, oldw, oldh)

    }


    private fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cornerRadius = 0

        rect.apply {
            this.left = mLeft
            this.top = mTop
            this.right = dpToPx(rectWidth) + mLeft
            this.bottom = dpToPx(rectHeight) + mTop
        }

        canvas?.drawRoundRect(rect, cornerRadius.toFloat(), cornerRadius.toFloat(), eraser)

        // draw horizontal line

        line.color = lineColor
        line.strokeWidth = lineWidth.toFloat()

        // draw the line to product animation
        if (endY >= mTop + dpToPx(rectHeight).toFloat() + frames.toFloat()) {
            revAnimation = true
        } else if (endY == mTop + frames) {
            revAnimation = false
        }

        // check if the line has reached to bottom
        if (revAnimation) {
            endY -= frames.toFloat()
        } else {
            endY += frames.toFloat()
        }

        canvas?.drawLine(mLeft, endY, mLeft + dpToPx(rectWidth), endY, line)
        invalidate()
    }


}