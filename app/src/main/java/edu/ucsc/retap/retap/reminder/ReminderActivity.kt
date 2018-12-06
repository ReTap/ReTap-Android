package edu.ucsc.retap.retap.reminder

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.common.BaseActivity
import edu.ucsc.retap.retap.database.ReminderContract

class ReminderActivity : BaseActivity() {
    companion object {
        private val TAG = ReminderActivity::class.java.simpleName
    }
    private val mAdapter:ArrayAdapter<String>
    ReminderDbHelper = mHelper
    mReminderListView = findViewById<ListView>(R.id.list_reminder)

    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        menuInflater.inflate(R.menu.menu_reminder, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun updateUI() {
        val taskList = ArrayList()
        val db = mHelper.getReadableDatabase()
        val cursor = db.query(ReminderContract.ReminderEntry.table,
            arrayOf<String>(ReminderContract.ReminderEntry._ID, ReminderContract.ReminderEntry.columnReminderTitle), null, null, null, null, null)
        while (cursor.moveToNext())
        {
            val idx = cursor.getColumnIndex(ReminderContract.ReminderEntry.columnReminderTitle)
            taskList.add(cursor.getString(idx))
        }
        if (mAdapter == null)
        {
            mAdapter = ArrayAdapter(this,
                R.layout.item_todo,
                R.id.reminder_title,
                taskList)
            mReminderListView.setAdapter(mAdapter)
        }
        else
        {
            mAdapter.clear()
            mAdapter.addAll(taskList)
            mAdapter.notifyDataSetChanged()
        }
        cursor.close()
        db.close()
    }

    fun deleteTask(view:View) {
        val parent = view.getParent() as View
        val taskTextView = parent.findViewById(R.id.task_title) as TextView
        val task = String.valueOf(taskTextView.getText())
        val db = mHelper.getWritableDatabase()
        db.delete(ReminderContract.ReminderEntry.table,
            ReminderContract.ReminderEntry.columnReminderTitle + " = ?",
            arrayOf<String>(task))
        db.close()
        updateUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        updateUI()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        mReminderListView = findViewById<ListView>(R.id.list_reminder)

        mHelper = new ReminderDbHelper(this)
        SQLiteDatabase db = mHelper.getReadableDatabase()
        Cursor cursor = db.query(ReminderContract.ReminderEntry.table, arrayOf(ReminderContract.ReminderEntry._ID,
            ReminderContract,ReminderEntry.columnReminderTitle), null, null, null, null, null);
        while(cursor.moveToNext()) {
            var idx = cursor.getColumnIndex(ReminderContract.ReminderEntry.columnReminderTitle)
        }
        cursor.close
        db.close

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                val taskEditText = EditText(this)
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Add a Reminder")
                    .setMessage("Keep It Short")
                    .setView(taskEditText)
                    .setPositiveButton("Add", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog:DialogInterface, which:Int) {
                            updateUI()
                            val task = taskEditText.getText().toString()
                            SQLiteDatabase db = mHelper.getWritableDatabase()
                            ContentValues values = new contentValues()
                            values.put(ReminderContract.ReminderEntry.table, null, values, SQLiteDatabase.CONFLICT_REPLACE)
                            db.close()
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun layoutId(): Int = R.layout.activity_reminder
}
