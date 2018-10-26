package edu.ucsc.retap.retap.messages.view

import android.view.View
import edu.ucsc.retap.retap.R

class MessagesViewModule(rootView: View) {
    val loading = rootView.findViewById<View>(R.id.loading)

    fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loading.visibility = View.GONE
    }
}