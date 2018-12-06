package edu.ucsc.retap.retap.compose

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.common.BaseActivity
import edu.ucsc.retap.retap.morse.MorseHelper

class ComposeActivity: BaseActivity() {
    companion object {
        private const val PLAIN_TEXT = "text/plain"
    }

    private lateinit var textView: TextView
    private val currentMorseText = SpannableStringBuilder()
    private var inPlaintextMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textView = findViewById(R.id.text_view)

        val dot = findViewById<View>(R.id.dot)
        val dash = findViewById<View>(R.id.dash)
        val space = findViewById<View>(R.id.space)

        dot.setOnClickListener {
            MorseHelper.appendMorse(true, this, currentMorseText)
            updateText()
        }

        dash.setOnClickListener {
            MorseHelper.appendMorse(false, this, currentMorseText)
            updateText()
        }

        space.setOnClickListener {
            currentMorseText.append(" ")
            updateText()
        }

        textView.setOnClickListener {
            if (currentMorseText.isEmpty()) {
                return@setOnClickListener
            }
            currentMorseText.delete(currentMorseText.length - 1, currentMorseText.length)
            updateText()
        }

        textView.setOnLongClickListener {
            if (inPlaintextMode) {
                textView.setText(currentMorseText, TextView.BufferType.SPANNABLE)
            } else {
                textView.text = MorseHelper.convertToText(currentMorseText.toString())
            }
            inPlaintextMode = !inPlaintextMode
            true
        }

        textView.movementMethod = ScrollingMovementMethod()
    }

    override fun actionButtonDrawable(): Drawable? = resources.getDrawable(R.drawable.ic_share)

    override fun isActionButtonEnabled(): Boolean = true

    override fun onActionButtonClicked() {
        val text = MorseHelper.convertToText(currentMorseText.toString())
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = PLAIN_TEXT
        startActivity(intent)
    }

    private fun updateText() {
        textView.setText(currentMorseText, TextView.BufferType.SPANNABLE)
        inPlaintextMode = false
    }

    @LayoutRes
    override fun layoutId(): Int = R.layout.activity_compose

    override fun navigationTitleText(): String = "Compose"
}
