package edu.ucsc.retap.retap.common

import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.inbox.InboxActivity
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

        val navigationAction: ImageView = findViewById(R.id.action_icon)
        if (isActionButtonEnabled()) {
            navigationAction.visibility = View.VISIBLE
            navigationAction.setOnClickListener {
                onActionButtonClicked()
            }
            navigationAction.setImageDrawable(actionButtonDrawable())
        } else {
            navigationAction.visibility = View.GONE
        }

        val navigationTitle: TextView = findViewById(R.id.navigation_title)
        navigationTitle.text = navigationTitleText()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_inbox -> {
                    if (this !is InboxActivity) {
                        val intent = Intent(this, InboxActivity::class.java)
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

    abstract fun navigationTitleText(): String

    open protected fun actionButtonDrawable(): Drawable? = null

    open protected fun isActionButtonEnabled(): Boolean = false

    open protected fun onActionButtonClicked() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
