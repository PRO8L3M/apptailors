package com.apptailors.streams.common.customs

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputEditText
import com.apptailors.streams.R
import com.apptailors.streams.ext.hideKeyboard

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    private var listener: OnEditorActionListener? = OnEditorActionListener { _, actionId, _ ->
        var handled = false
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard()
            clearFocus()
            handled = true
        }
        handled
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setOnEditorActionListener(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        listener = null
    }
}