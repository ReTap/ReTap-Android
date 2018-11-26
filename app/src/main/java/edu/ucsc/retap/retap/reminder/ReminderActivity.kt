package edu.ucsc.retap.retap.reminder

import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
                Log.d(TAG, "Add a new task")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun layoutId(): Int = R.layout.activity_reminder
}
