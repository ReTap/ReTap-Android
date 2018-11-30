package edu.ucsc.retap.retap.common

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.annotation.LayoutRes
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.conversations.ConversationsActivity
import edu.ucsc.retap.retap.reminder.ReminderActivity
import edu.ucsc.retap.retap.settings.SettingsActivity

/**
 * Activity at the base level. Includes the app's navigation drawer.
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        drawerLayout = findViewById(R.id.drawer_layout)
        LayoutInflater.from(this).inflate(layoutId(), findViewById(R.id.content_frame), true)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_inbox -> {
                    if (this !is ConversationsActivity) {
                        val intent = Intent(this, ConversationsActivity::class.java)
                        startActivity(intent)
                    }

                }
                R.id.nav_reminders -> {
                    if (this !is ReminderActivity) {
                        val intent = Intent(this, ReminderActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.nav_settings -> {
                    if (this !is SettingsActivity) {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            true
        }
    }

    @LayoutRes
    abstract fun layoutId(): Int

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
