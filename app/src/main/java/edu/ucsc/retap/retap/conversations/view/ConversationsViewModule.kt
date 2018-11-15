package edu.ucsc.retap.retap.conversations.view

import android.view.View
import edu.ucsc.retap.retap.R

class ConversationsViewModule(rootView: View) {
    private val loading = rootView.findViewById<View>(R.id.loading)

    fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loading.visibility = View.GONE
    }
}