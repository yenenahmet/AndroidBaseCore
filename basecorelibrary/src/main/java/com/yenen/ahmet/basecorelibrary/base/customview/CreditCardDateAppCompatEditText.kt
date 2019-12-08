package com.yenen.ahmet.basecorelibrary.base.customview

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CreditCardDateAppCompatEditText constructor(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(5))
        this.maxLines =1
        this.addTextChangedListener(ReplaceableInputWatcher(this))
    }


    class ReplaceableInputWatcher constructor(private val editText: AppCompatEditText) :
        TextWatcher {


        override fun afterTextChanged(s: Editable?) {
            s?.let {
                editText.removeTextChangedListener(this)
                creditCardDateFormat(it)
                editText.addTextChangedListener(this)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }


        private fun creditCardDateFormat(s:Editable) {
            if (!isInputCorrect(s)) {
                editText.setText(concatString(getDigitArray(s)))
            }

        }

        private fun getDigitArray(s: Editable): CharArray {
            val digits = CharArray(5)
            var index = 0
            var i = 0
            while (i < s.length && index < 5) {
                val current = s[i]
                if (Character.isDigit(current)) {
                    digits[index] = current
                    index++
                }
                i++
            }
            return digits
        }


        private fun isInputCorrect(
            s: Editable
        ): Boolean {
            var isCorrect = s.length <= 5 // check size of entered string
            for (i in s.indices) { // check that every element is right
                isCorrect = if (i > 0 && (i + 1) % 3== 0) {
                    isCorrect and (' ' == s[i])
                } else {
                    isCorrect and Character.isDigit(s[i])
                }
            }
            return isCorrect
        }

        private fun concatString(digits: CharArray): String {
            val formatted = StringBuilder()

            for (i in digits.indices) {
                if (digits[i].toInt() != 0) {
                    formatted.append(digits[i])
                    if (i > 0 && i < digits.size - 1 && (i + 1) % 2 == 0) {
                        formatted.append('/')
                    }
                }
            }

            return formatted.toString()
        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        text?.length?.let {
            setSelection(it)
        }
    }

}