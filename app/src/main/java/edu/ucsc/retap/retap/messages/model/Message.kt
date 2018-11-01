package edu.ucsc.retap.retap.messages.model

import android.telephony.SmsMessage

/**
 * Contains information about a SMS message.
 */
data class Message(
    var sender: String,
    var contents: String
) {
    companion object {
        fun createFrom(smsMessage: SmsMessage): Message =
                Message(
                    smsMessage.displayOriginatingAddress,
                    smsMessage.displayMessageBody
                )
    }
}