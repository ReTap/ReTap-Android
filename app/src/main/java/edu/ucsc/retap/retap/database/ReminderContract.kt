package edu.ucsc.retap.retap.database

import android.provider.BaseColumns

class ReminderContract {
    val dbName = "edu.ucsc.retap.retap.database"
    val dbVersion = 1

    class ReminderEntry : BaseColumns {
        val table = "reminders"
        val columnReminderTitle = "title"
    }
}