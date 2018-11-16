package edu.ucsc.retap.retap.reminder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import edu.ucsc.retap.retap.R

class reminderActivity : AppCompatActivity() {
    private val TAG = "reminderActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
    }

    fun onCreateOptionsMenu(menu: Menu):Boolean {
        getMenuInflater().inflate(R.menu.menu_reminder, menu)
        return super.onCreateOptionsMenu(menu)
    }
    fun onOptionsItemSelected(item: MenuItem):Boolean {
        when (item.getItemId()) {
            R.id.action_add_task -> {
                Log.d(TAG, "Add a new task")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
