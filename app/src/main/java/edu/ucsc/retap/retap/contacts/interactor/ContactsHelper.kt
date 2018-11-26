package edu.ucsc.retap.retap.contacts.interactor

import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.content.ContentUris
import android.graphics.Bitmap
import android.content.Context
import android.graphics.Canvas
import android.net.Uri
import edu.ucsc.retap.retap.R
import java.io.IOException
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import edu.ucsc.retap.retap.contacts.model.Contact

object ContactsHelper {
    fun getContact(context: Context, number: String): Contact {
        val contentResolver = context.contentResolver
        var contactId: String? = null
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
                contactId = cursor
                        .getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
                displayName = cursor
                        .getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup
                                .DISPLAY_NAME))
            }
            cursor.close()
        }

        var photo = drawableToBitmap(context.resources.getDrawable(R.drawable.ic_profile))

        try {
            val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                    context.contentResolver,
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                            contactId?.toLong() ?: 0))

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream) ?: photo
            }
            inputStream?.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Contact(photo, displayName)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}