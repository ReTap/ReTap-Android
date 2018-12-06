package edu.ucsc.retap.retap.contacts.interactor

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import edu.ucsc.retap.retap.contacts.model.Contact

object ContactsHelper {
    fun getContact(context: Context, number: String): Contact {
        val contentResolver = context.contentResolver
        var displayName: String? = null
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number))

        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID)

        val cursor = contentResolver.query(
                uri,
                projection, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                displayName = cursor
                        .getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup
                                .DISPLAY_NAME))
            }
            cursor.close()
        }

        return Contact(displayName)
    }
}
