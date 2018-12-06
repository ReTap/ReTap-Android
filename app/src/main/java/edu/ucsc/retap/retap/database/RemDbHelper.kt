package edu.ucsc.retap.retap.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class RemDbHelper : SQLiteOpenHelper() {
    fun RemDbHelper(Context, context) {
        super(context, ReminderContract.dbName, null, ReminderContract.dbVersion)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "Create Table" + ReminderContract.ReminderEntry.table + "(" +
                ReminderContract.ReminderEntry._ID + "Integer Primary Key Autoincrement, " +
                ReminderContract.ReminderEntry.columnReminderTitle + "Text Not Null);"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("Drop table If Exists" + ReminderContract.ReminderEntry.table)
        onCreate(db)
    }

}