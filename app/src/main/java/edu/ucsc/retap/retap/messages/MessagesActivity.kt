package edu.ucsc.retap.retap.messages

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.messages.adapter.MessagesAdapter
import edu.ucsc.retap.retap.messages.interactor.MessagesInteractor
import edu.ucsc.retap.retap.messages.presenter.MessagesPresenter
import edu.ucsc.retap.retap.messages.view.MessagesViewModule
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.os.Vibrator
import android.view.KeyEvent
import android.view.Menu
import edu.ucsc.retap.retap.vibration.VibrationInteractor

class MessagesActivity : AppCompatActivity() {
    private companion object {
       private const val SMS_PERMISSION_CODE = 1
    }

    private lateinit var presenter: MessagesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        val recyclerView = findViewById<RecyclerView>(R.id.messages_view)
        val adapter = MessagesAdapter(LayoutInflater.from(this))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        presenter = MessagesPresenter(MessagesViewModule(findViewById(R.id.root)), adapter,
            MessagesInteractor(this), VibrationInteractor(vibrator))
    }

    override fun onStart() {
        super.onStart()
        if (!isSMSPermissionGranted()) {
            requestReadSMSPermission()
        } else {
            presenter.loadMessages()
        }
    }

    override fun onDestroy() {
        presenter.cleanUp()
        // Super call always goes last.
        super.onDestroy()
    }

    private fun isSMSPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) ==
                        PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.loadMessages()
                }
                return
            }
        }
    }

    private fun requestReadSMSPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                presenter.onVolumeDown()
                return true
            }
            KeyEvent.KEYCODE_VOLUME_UP -> {
                presenter.onVolumeUp()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_messages, menu)

        val pauseAction = menu?.findItem(R.id.action_pause)
        pauseAction?.setOnMenuItemClickListener {
            presenter.pauseVibration()
            return@setOnMenuItemClickListener true
        }

        return super.onCreateOptionsMenu(menu)
    }
}
