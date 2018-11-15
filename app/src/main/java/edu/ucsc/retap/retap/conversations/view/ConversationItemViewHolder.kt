package edu.ucsc.retap.retap.conversations.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import edu.ucsc.retap.retap.R

class ConversationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val senderTextView = itemView.findViewById<TextView>(R.id.sender)
    private val contentTextView = itemView.findViewById<TextView>(R.id.body)

    fun setSenderText(sender: String) {
        senderTextView.text = sender
    }

    fun setContentText(content: String) {
        contentTextView.text = content
    }

    fun setSelected(isSelected: Boolean) {
        itemView.setBackgroundResource(
            if (isSelected) {
                R.color.selectedColor
            } else {
                android.R.color.white
            }
        )
    }
}
