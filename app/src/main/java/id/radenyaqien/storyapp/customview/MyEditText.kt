package id.radenyaqien.storyapp.customview

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import id.radenyaqien.storyapp.R

class MyEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var clearIcon: Drawable


    constructor(context: android.content.Context) : super(context) {
        init()
    }

    constructor(context: android.content.Context, attrs: android.util.AttributeSet) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: android.content.Context,
        attrs: android.util.AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        clearIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
        doAfterTextChanged { s ->
            if (s.toString().isNotEmpty()) showClearIcon() else hideClearIcon()
        }
        setOnTouchListener(this)
    }

    private fun hideClearIcon() {
        setClearIcon()
    }

    private fun showClearIcon() {
        setClearIcon(endOfTheText = clearIcon)
    }

    private fun setClearIcon(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }



    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                val clearButtonEnd: Float = (clearIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                val clearButtonStart: Float =
                    (width - paddingEnd - clearIcon.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearIcon = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_close_24
                        ) as Drawable
                        showClearIcon()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearIcon = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_close_24
                        ) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearIcon()
                        return true
                    }
                    else -> return false
                }
            }
            return false
        }
        return false
    }

}
