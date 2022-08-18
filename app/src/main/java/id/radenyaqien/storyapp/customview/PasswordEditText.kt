package id.radenyaqien.storyapp.customview

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import id.radenyaqien.storyapp.R

class PasswordEditText : TextInputEditText {

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
        transformationMethod = PasswordTransformationMethod.getInstance()
        doAfterTextChanged {
            text?.let { validateJumlahChar(it) }
        }
    }

    private fun validateJumlahChar(text: CharSequence) {
        val isLessThanSixChar = text.length < 6
        if (isLessThanSixChar) {
            error = context.getString(R.string.password_error)
        }
    }

}
