package edu.ucsc.retap.retap.messages.interactor

import android.content.Context
import android.net.Uri
import edu.ucsc.retap.retap.messages.model.Message
import io.reactivex.Single

class MessagesInteractor(private val context: Context) {
    companion object {
        private const val SMS_CONTENT_RESOLVER_URI = "content://sms/inbox"
    }

    /**
     * Messages take a long time to get. Do it async and off the main thread with an observable.
     */
    fun getSMSMessages(): Single<List<Message>> {
        return Single.fromCallable {
            val smsMessages = ArrayList<Message>()

            val uri = Uri.parse(SMS_CONTENT_RESOLVER_URI)
            val cursor = context.contentResolver.query(uri, null, null, null, null)

            cursor ?: throw(NullPointerException("Cursor should not be null"))

            // Read the sms data and store it in the list
            if (cursor.moveToFirst()) {
                for (i in 0 until cursor.count) {
                    val sender = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString()
                    val contents = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString()
                    val newMessage = Message(sender, contents)
                    smsMessages.add(newMessage)
                    cursor.moveToNext()
                }
            }
            cursor.close()
            smsMessages
        }
    }
}