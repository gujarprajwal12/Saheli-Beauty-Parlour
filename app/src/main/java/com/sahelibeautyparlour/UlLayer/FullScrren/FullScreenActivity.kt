package com.sahelibeautyparlour.UlLayer.FullScrren


import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sahelibeautyparlour.R

class FullScreenActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var scaleDetector: ScaleGestureDetector
    private val matrix = Matrix()

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var currentTouchX = 0f
    private var currentTouchY = 0f

    private var scaleFactor = 1f
    private val maxScaleFactor = 5f
    private val minScaleFactor = 0.5f

    private var isZooming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        imageView = findViewById(R.id.fullScreenImageView)

        // Get the image URL passed from the previous activity
        val imageUrl = intent.getStringExtra("image_url")

        // Load the image into the ImageView using Glide
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // Initialize ScaleGestureDetector for pinch-to-zoom gestures
        scaleDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = maxOf(minScaleFactor, minOf(scaleFactor, maxScaleFactor))

                // Apply zoom effect to the image
                applyZoom(scaleFactor)
                return true
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { scaleDetector.onTouchEvent(it) }

        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Save the initial touch coordinates for sliding
                lastTouchX = event.x
                lastTouchY = event.y
                isZooming = false
            }

            MotionEvent.ACTION_MOVE -> {
                // If zooming, don't allow sliding
                if (scaleDetector.isInProgress) {
                    isZooming = true
                }

                // If not zooming, handle sliding
                if (!isZooming) {
                    currentTouchX = event.x
                    currentTouchY = event.y

                    val dx = currentTouchX - lastTouchX
                    val dy = currentTouchY - lastTouchY

                    // Apply sliding (translation) to the image
                    matrix.postTranslate(dx, dy)
                    imageView.imageMatrix = matrix

                    // Update the last touch coordinates for the next move
                    lastTouchX = currentTouchX
                    lastTouchY = currentTouchY
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Reset zooming status after touch release
                isZooming = false
            }
        }
        return true
    }

    // Apply zooming to the image using the scale factor
    private fun applyZoom(scaleFactor: Float) {
        matrix.setScale(scaleFactor, scaleFactor)
        imageView.imageMatrix = matrix
    }


}
