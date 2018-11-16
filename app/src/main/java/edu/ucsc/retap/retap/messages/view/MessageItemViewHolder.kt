package edu.ucsc.retap.retap.messages.view

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import edu.ucsc.retap.retap.R

class MessageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val senderTextView = itemView.findViewById<TextView>(R.id.sender)
    private val contentTextView = itemView.findViewById<TextView>(R.id.body)
    private val profileImageView = itemView.findViewById<ImageView>(R.id.profile_image)

    fun setSenderText(sender: String) {
        senderTextView.text = sender
    }

    fun setContentText(content: String) {
        contentTextView.text = content
    }

    fun setProfileImage(profileImage: Bitmap) {
        profileImageView?.setImageBitmap(profileImage)
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
