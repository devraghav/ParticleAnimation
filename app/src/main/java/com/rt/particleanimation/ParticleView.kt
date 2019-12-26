package com.rt.particleanimation

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.util.*


class ParticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var timer: Timer = Timer()
    val random=Random()
    val colorList= mutableSetOf<Int>()
    init {
        timer.scheduleAtFixedRate(AddViewTimer(), 0, 1000)
        colorList.add(ContextCompat.getColor(context,R.color.green))
        colorList.add(ContextCompat.getColor(context,R.color.red))

    }


    inner class AddViewTimer : TimerTask() {
        override fun run() {
            val activity=context as AppCompatActivity
            activity.runOnUiThread {
                val textView = emitter()
                addView(textView)
            }

        }

    }

    private fun emitter(): ImageView {
        val frameLayoutParams = FrameLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen._24dp),
            resources.getDimensionPixelSize(R.dimen._24dp)
        )
        val textView = ImageView(context)
        textView.setImageResource(R.drawable.ic_android_black_24dp)
        textView.layoutParams = frameLayoutParams
        textView.x = random.nextInt(600).toFloat()
        textView.y = -10f

        textView.setColorFilter(colorList.shuffled()[0])
        textView.animate()
            .translationY(1200f)
            .rotation(720f)
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(3000)
            .setInterpolator(LinearInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    removeView(textView)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .start()
        return textView
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer.cancel()
    }
}