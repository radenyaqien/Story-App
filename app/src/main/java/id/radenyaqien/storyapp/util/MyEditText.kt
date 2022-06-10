package id.radenyaqien.storyapp.util

import android.graphics.Canvas
import androidx.appcompat.widget.AppCompatEditText
import id.radenyaqien.storyapp.R

class MyEditText : AppCompatEditText {
    constructor(context: android.content.Context) : super(context) {}
    constructor(context: android.content.Context, attrs: android.util.AttributeSet) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: android.content.Context,
        attrs: android.util.AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas?) {
        error = if (text?.length!! < 6) {
            "Password must be at least 6 characters"
        } else {
            null
        }
        super.onDraw(canvas)
    }

    override fun setError(error: CharSequence?) {
        if (error == null) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_error_outline_24,
                0
            )
        }
    }
}
