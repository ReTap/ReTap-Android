package edu.ucsc.retap.retap.messages.view

import android.graphics.PorterDuff
import android.view.View
import android.widget.ProgressBar
import edu.ucsc.retap.retap.R

class MessagesViewModule(rootView: View) {
    private val loading = rootView.findViewById<View>(R.id.loading)

    init {
        val progressBarTintColor = rootView.resources.getColor(R.color.purpleColor)
        rootView.findViewById<ProgressBar>(R.id.progress_bar).indeterminateDrawable
                .setColorFilter(progressBarTintColor, PorterDuff.Mode.SRC_IN)
    }
    fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loading.visibility = View.GONE
    }
}