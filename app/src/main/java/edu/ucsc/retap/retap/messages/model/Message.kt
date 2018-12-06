package edu.ucsc.retap.retap.messages.model

/**
 * Contains information about a SMS message.
 */
data class Message(
        val displayName: String?,
        val sender: String,
        val contents: String,
        val sent: Long
)