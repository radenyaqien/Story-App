package id.radenyaqien.storyapp.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import id.radenyaqien.storyapp.R
import java.util.regex.Pattern

class EmailEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var clearIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {
        clearIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
        setSingleLine()

        doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) showClearIcon() else hideClearIcon()
        }
        doAfterTextChanged { p0 ->
            if (p0?.toString()?.isNotEmpty() == true) {
                if (!p0.toString().emailValidate()) {
                    error = "Email Tidak Valid"
                } else {
                    showClearIcon()
                }
            }
        }

        setOnTouchListener(this)

    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {

        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearIcon.intrinsicWidth).toFloat()
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


    private fun showClearIcon() {
        setClearIcon(endOfTheText = clearIcon)
    }

    private fun hideClearIcon() {
        setClearIcon()
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

    private fun String.emailValidate(): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(this).matches()
    }

}
