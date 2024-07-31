package com.example.bounceball

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat

class GameView(var c: Context, private var gameTask: GameTask) : View(c) {
    // Declaration of variables
    private var speed = 20
    private var score = 0

    // Ball properties
    private var ballDrawable = ContextCompat.getDrawable(context, R.drawable.ball) // Ball image
    private var ballRadius: Int = convertCmToPx(0.25f) // Ball radius in pixels

    private var ballX = 0
    private var ballY = 0
    private var ballSpeedX = 10
    private var ballSpeedY = 10

    // Horizontal line properties
    private var lineX = 0
    private var lineY = 0
    private val lineLength = 200
    private val lineHeight = 20

    // Animation control variables
    private var isRunning = false
    private var colorChangingHandler: Handler = Handler(Looper.getMainLooper())

    // Override onDraw method to draw the view
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(ContextCompat.getColor(context, R.color.background_color))

        // Draw the ball image
        ballDrawable?.let {
            it.setBounds(ballX, ballY, ballX + ballRadius * 2, ballY + ballRadius * 2)
            it.draw(canvas)
        }

        // Draw the horizontal line
        canvas.drawRect(Rect(lineX, lineY, lineX + lineLength, lineY + lineHeight), Paint())
    }

    // Handle touch events
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Move the horizontal line to the touch position
                lineX = event.x.toInt() - lineLength / 2
                lineY = height - 200 // Adjust this value according to your layout
                invalidate()
            }
        }
        return true
    }
    // Function to start the animation
    fun startAnimation() {
        isRunning = true

        // Set the initial position of the horizontal line to the bottom of the screen
        lineY = height - 200 // Adjust this value according to your layout

        val runnable = object : Runnable {
            override fun run() {
                if (isRunning) {
                    update()
                    invalidate()
                    postDelayed(this, 16) // 60 frames per second
                }
            }
        }
        post(runnable)
    }

    fun stopAnimation() {
        isRunning = false
    }

    // Function to update the game state
    private fun update() {
        // Update ball position
        ballX += ballSpeedX
        ballY += ballSpeedY

        // Check collision with walls
        if (ballX <= 0 || ballX >= width) {
            ballSpeedX *= -1
        }
        if (ballY <= 0 || (ballY + ballRadius >= lineY && ballY - ballRadius <= lineY + lineHeight && ballX + ballRadius >= lineX && ballX - ballRadius <= lineX + lineLength)) {
            ballSpeedY *= -1
            if (ballY >= lineY - ballRadius) {
                // Ball hits the horizontal line, increase score and speed
                updateScoreAndSpeed()
            }
        }


        // Check if ball is out of bounds
        if (ballY >= height) {
            gameOver()
        }
    }

    // Function to update score and speed when the ball hits the line
    private fun updateScoreAndSpeed() {
        score++
        speed = 1 + score / 2
        ballSpeedY += 2 // Increase bounce height
        //Toast.makeText(context, "Score: $score", Toast.LENGTH_SHORT).show()
        // Increase vertical speed
        ballSpeedY = calculateVerticalSpeed(ballSpeedY)
    }

    private fun calculateVerticalSpeed(currentSpeed: Int): Int {
        val verticalSpeedIncrement = 2
        return currentSpeed + verticalSpeedIncrement
    }

    //Function to handle game over
    private fun gameOver() {
        stopAnimation()
        gameTask.closeGame(score)
    }

    // Function to convert centimeters to pixels
    private fun convertCmToPx(cm: Float): Int {
        val metrics: DisplayMetrics = resources.displayMetrics
        return (cm * metrics.densityDpi / 2.54f).toInt()
    }
}