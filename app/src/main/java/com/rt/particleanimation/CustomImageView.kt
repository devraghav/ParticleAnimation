package com.rt.particleanimation

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.ContextCompat


class CustomImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    var color: Int = ContextCompat.getColor(context, R.color.green)


    fun setImageColor(colorValue: Int) {
        color = colorValue
        setColorFilter(color)
    }
}