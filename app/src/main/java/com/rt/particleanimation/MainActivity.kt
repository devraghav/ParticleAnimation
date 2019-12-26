package com.rt.particleanimation

import android.R.attr
import android.animation.Animator
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rt.particleanimation.viewmodels.TimerViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var distance = 0f
    private lateinit var timberViewModel: TimerViewModel
    private val random = Random()
    private val colorList = mutableSetOf<Int>()
    private var width = 0;
    private var duration = 3000L;
    private var scale = 1.2f;
    private var rotation = 720f;
    private var isGreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        timberViewModel = ViewModelProviders.of(this)[TimerViewModel::class.java]
        colorList.add(ContextCompat.getColor(this, R.color.green))
        colorList.add(ContextCompat.getColor(this, R.color.red))

        Handler().postDelayed({
            val oneRect = calculateRectOnScreen(textView)
            val otherRect = calculateRectOnScreen(button2)
            distance = Math.abs(oneRect!!.bottom - otherRect!!.top)
            startParticles()
        }, 300)


    }

    private fun startParticles() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels

        timberViewModel.timberObservable.observe(this, Observer {
            val imageView = emitter()
            rootView.addView(imageView)
        })

        timberViewModel.startTimber()

        button2.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.green))
        button2.setOnClickListener {
            it.setBackgroundColor(
                if (isGreen) {
                    isGreen = false
                    ContextCompat.getColor(this@MainActivity, R.color.red)
                } else {
                    isGreen = true
                    ContextCompat.getColor(this@MainActivity, R.color.green)
                }
            )
            //            FlingAnimation(textView, DynamicAnimation.Y).apply {
            //                setStartVelocity(1000f)
            //                setMaxValue(distance)
            //                friction = 0.10f
            //                start()
            //            }.addEndListener { animation, canceled, value, velocity ->
            //                Log.d("Touched","Touched")
            //                Log.d("Touched","distance"+distance.toString())
            //                Log.d("Touched","valued"+value.toString())
            //            }
        }
    }

    private fun calculateRectOnScreen(view: View): RectF? {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return RectF(
            location[0].toFloat(),
            location[1].toFloat(),
            (location[0] + view.measuredWidth).toFloat(),
            (location[1] + view.measuredHeight).toFloat()
        )


    }


    private fun emitter(): ImageView {
        val frameLayoutParams = FrameLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen._24dp),
            resources.getDimensionPixelSize(R.dimen._24dp)
        )
        val textView = CustomImageView(this)
        textView.setImageResource(R.drawable.ic_android_black_24dp)
        textView.layoutParams = frameLayoutParams
        textView.x = random.nextInt(width - 100).toFloat()
        textView.y = -10f

        textView.setImageColor(colorList.shuffled()[0])
        textView.animate()
            .translationY(distance + 10)
            .rotation(rotation)
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(duration)
            .setInterpolator(LinearInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {

                    val greenColor=ContextCompat.getColor(this@MainActivity,R.color.green)
                    val isImageColorGreen=textView.color==greenColor
                    if (isGreen==isImageColorGreen) {
                        Log.d("test", "matched")
                        reverseAnimation(textView)

                    } else {
                        Log.d("test", "unMatched")
                         rootView.removeView(textView)
                    }

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .start()
        return textView
    }

    private fun reverseAnimation(textView: CustomImageView) {
        textView.animate()
            .translationY(-200f)
            .alpha(0f)
            .setDuration(300)
            .setListener(object :Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    rootView.removeView(textView)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .start()
    }

}
