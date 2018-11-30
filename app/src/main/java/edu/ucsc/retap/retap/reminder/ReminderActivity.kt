package edu.ucsc.retap.retap.reminder

import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.common.BaseActivity

class ReminderActivity : BaseActivity() {
    companion object {
        private val TAG = ReminderActivity::class.java.simpleName
    }

    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        menuInflater.inflate(R.menu.menu_reminder, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                val taskEditText = EditText(this)
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Add a Reminder")
                    .setMessage("Keep It Short")
                    .setView(taskEditText)
                    .setPositiveButton("Add", object: DialogInterface.OnClickListener() {
                        fun onClick(dialog:DialogInterface, which:Int) {
                            val task = String.valueOf(taskEditText.getText())
                            Log.d(TAG, "Task to add: " + task)
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun layoutId(): Int = R.layout.activity_reminder
}
