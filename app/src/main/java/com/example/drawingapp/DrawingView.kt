package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var mDrawPath = Path()
    private var mCanvasBitmap: Bitmap? = null
    private var mStartX: Float = 0.0f
    private var mStartY: Float = 0.0f
    private var touchX: Float = 0.0f
    private var touchY: Float = 0.0f

    private var isDrawing = false

    init {
        setupDrawing()
    }

    companion object {
        var mDrawPaint = Paint()
        var currentColorShape = R.color.black
        var currentShape = Shapes.DRAW_A_STROKE_SHAPE
        lateinit var canvas: Canvas
    }

    private fun setupDrawing() {
        mDrawPaint.isAntiAlias = true
        mDrawPaint.isDither = true
        mDrawPaint.color = currentColorShape
        mDrawPaint.style = Paint.Style.STROKE
        mDrawPaint.strokeJoin = Paint.Join.ROUND
        mDrawPaint.strokeCap = Paint.Cap.ROUND
        mDrawPaint.strokeWidth = 8F
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mDrawPaint)

        if (isDrawing) {
            when (currentShape) {
                Shapes.DRAW_A_STROKE_SHAPE -> {

                }

                Shapes.DRAW_AN_ARROW_SHAPE -> {
                    onDrawArrow(canvas)
                }

                Shapes.DRAW_A_RECTANGLE_SHAPE -> {
                    onDrawRectangle(canvas)
                }

                Shapes.DRAW_A_CIRCLE_SHAPE -> {
                    onDrawOval(canvas)
                }
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y

        when (currentShape) {
            Shapes.DRAW_A_STROKE_SHAPE -> {
                onTouchEventLine(event)
            }

            Shapes.DRAW_AN_ARROW_SHAPE -> {
                onTouchEventArrow(event)
            }

            Shapes.DRAW_A_RECTANGLE_SHAPE -> {
                onTouchEventRectangle(event)
            }

            Shapes.DRAW_A_CIRCLE_SHAPE -> {
                onTouchEventOval(event)
            }
        }
        return true
    }

    private fun onTouchEventLine(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                mStartX = touchX
                mStartY = touchY

                mDrawPath.reset()
                mDrawPath.moveTo(touchX, touchY)

                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {

                mDrawPath.quadTo(
                    mStartX,
                    mStartY,
                    (touchX + mStartX) / 2,
                    (touchY + mStartY) / 2
                )
                mStartX = touchX
                mStartY = touchY

                canvas.drawPath(mDrawPath, mDrawPaint)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                isDrawing = false
                mDrawPath.lineTo(mStartX, mStartY)
                canvas.drawPath(mDrawPath, mDrawPaint)
                mDrawPath.reset()
                invalidate()
            }
        }
    }

    private fun onTouchEventArrow(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                mStartX = touchX
                mStartY = touchY
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                isDrawing = false
                drawArrow(canvas, mDrawPaint)
                invalidate()

            }
        }

    }

    private fun drawArrow(canvas: Canvas?, mPaint: Paint) {
        mPaint.color = currentColorShape
        val angleRad: Float
        val radius = 30f
        val angle = 35f

        angleRad = ((3.14 * angle / 180.0f).toFloat())
        val lineAngle: Float = atan2(touchY - mStartY, touchX - mStartX)

        canvas?.drawLine(mStartX, mStartY, touchX, touchY, mPaint)

        val aPath = Path()
        aPath.fillType = Path.FillType.EVEN_ODD

        aPath.moveTo(touchX, touchY)
        aPath.lineTo(
            (touchX - radius * cos(lineAngle - angleRad / 2.0)).toFloat(),
            (touchY - radius * sin(lineAngle - angleRad / 2.0)).toFloat()
        )
        aPath.lineTo(
            (touchX - radius * cos(lineAngle + angleRad / 2.0)).toFloat(),
            (touchY - radius * sin(lineAngle + angleRad / 2.0)).toFloat()
        )
        aPath.close()

        canvas?.drawPath(aPath, mPaint)
    }

    private fun onDrawArrow(canvas: Canvas?) {
        drawArrow(canvas, mDrawPaint)
    }

    private fun onTouchEventRectangle(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                mStartX = touchX
                mStartY = touchY
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                isDrawing = false
                drawRectangle(canvas, mDrawPaint)
                invalidate()
            }
        }
    }

    private fun onDrawRectangle(canvas: Canvas?) {
        drawRectangle(canvas, mDrawPaint)
    }

    private fun drawRectangle(mCanvas: Canvas?, mPaint: Paint) {
        mPaint.color = currentColorShape
        val right = if (mStartX > touchX) mStartX else touchX
        val left = if (mStartX > touchX) touchX else mStartX
        val bottom = if (mStartY > touchY) mStartY else touchY
        val top = if (mStartY > touchY) touchY else mStartY
        mCanvas?.drawRect(left, top, right, bottom, mPaint)
    }

    private fun onTouchEventOval(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                mStartX = touchX
                mStartY = touchY
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                isDrawing = false
                canvas.drawOval(mStartX, mStartY, touchX, touchY, mDrawPaint)

                invalidate()
            }
        }
    }

    private fun onDrawOval(canvas: Canvas?) {
        canvas?.drawOval(mStartX, mStartY, touchX, touchY, mDrawPaint)
    }
}