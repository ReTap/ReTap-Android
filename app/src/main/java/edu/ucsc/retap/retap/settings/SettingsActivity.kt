package edu.ucsc.retap.retap.settings

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ToggleButton
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.common.BaseActivity
import edu.ucsc.retap.retap.common.Constants

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val toggleButton = findViewById<ToggleButton>(R.id.toggle_button)
        toggleButton.isChecked = sharedPreferences
                .getBoolean(Constants.PREF_VIBRATE_ON_RECEIVE, false)
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences
                    .edit()
                    .putBoolean(Constants.PREF_VIBRATE_ON_RECEIVE, isChecked)
                    .apply()
            }
    }

    override fun layoutId(): Int = R.layout.activity_settings
}
