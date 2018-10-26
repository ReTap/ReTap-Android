package edu.ucsc.retap.retap.messages.model

/**
 * Contains information about a SMS message.
 */
data class Message(
    var sender: String,
    var contents: String
)