package edu.ucsc.retap.retap.messages.model

import android.graphics.Bitmap

/**
 * Contains information about a SMS message.
 */
data class Message(
        val profile: Bitmap,
        val displayName: String?,
        val sender: String,
        val contents: String,
        val sent: Long
)